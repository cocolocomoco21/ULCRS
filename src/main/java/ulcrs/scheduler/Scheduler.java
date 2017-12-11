package ulcrs.scheduler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulcrs.data.DataParse;
import ulcrs.data.DataStore;
import ulcrs.data.ParsedULCResponse;
import ulcrs.models.course.Course;
import ulcrs.models.rank.Rank;
import ulcrs.models.schedule.Node;
import ulcrs.models.schedule.Schedule;
import ulcrs.models.schedule.Tuple;
import ulcrs.models.shift.Assignment;
import ulcrs.models.shift.ScheduledShift;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;
import ulcrs.models.tutor.TutorPreferences;
import ulcrs.models.tutor.TutorStatus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * This class represents the scheduling algorithm.
 */
public class Scheduler {
    private static Logger log = LoggerFactory.getLogger(Scheduler.class);

    private static final int GENERATE_SCHEDULE_SIZE = 1;    // Let's make this 1 for now for simplicity
    private static final int DESIRED_COST_LIMIT = 10;

    private static List<Tutor> tutorsFromInput;
    private static List<Course> coursesFromInput;
    private static List<Shift> shiftsFromInput;

    private static List<Schedule> generatedSchedules;

    private static PriorityQueue<Node> frontier;
    private static HashSet<Node> explored;

    protected static List<Schedule> schedule(List<Tutor> tutors, List<Course> courses, List<Shift> shifts) {
        tutorsFromInput = tutors;
        coursesFromInput = courses;
        shiftsFromInput = shifts;

        // Start scheduling algorithm
        int desiredCost = 0;
        Node root = generateRoot(tutors, courses, shifts);
        Set<Node> result = null;

        // TODO this can be better
        while (result == null && desiredCost <= DESIRED_COST_LIMIT) {
            result = search(root, desiredCost++);
        }

        // TODO this can also be better
        if (result == null) {
            // Add Ghost tutor
            Node rootWithGhost = generateRootWithGhostTutor(tutors, courses, shifts);
            desiredCost = 0;

            while (result == null && desiredCost <= DESIRED_COST_LIMIT) {
                result = search(rootWithGhost, desiredCost++);
            }
        }

        // Finish scheduling algorithm, turn Nodes into list of Schedules
        generatedSchedules = new ArrayList<>();
        if (result != null) {
            for (Node node : result) {
                generatedSchedules.add(scheduleFromNode(node));
            }
        }

        return generatedSchedules;
    }

    private static Set<Node> search(Node inputNode, int desiredCost) {
        log.info("Searching at desiredCost: " + desiredCost);
        frontier = new PriorityQueue<>(Comparator.comparingInt(Node::getCost));
        explored = new HashSet<>();

        frontier.add(inputNode);

        Set<Node> success = new HashSet<>();

        while (frontier.size() != 0) {
            Node node = frontier.remove();
            explored.add(node);

            int parentCost = generateCost(node);
            log.info("Removing from frontier with cost " + parentCost+ " | remaining nodes on frontier: " + frontier.size());

            List<Node> successors = successors(node);
            for (Node successor : successors) {
                int cost = generateCost(successor);
                if (cost <= desiredCost) {
                    success.add(successor);
                    if (success.size() >= GENERATE_SCHEDULE_SIZE) {
                        return success;
                    }
                }

                if (cost < parentCost && !frontier.contains(successor)) {
                    frontier.add(successor);
                    log.info("Adding to frontier | parentCost: " + parentCost + " | childCost: " + cost + " | remaining nodes on frontier: " + frontier.size());
                }
            }
        }

        return success.isEmpty() ? null : success;
    }

    private static List<Node> successors(Node node) {
        List<Node> successorCandidates = new ArrayList<>();
        List<Node> successors = new ArrayList<>();

        int parentCost = node.generateCost();

        // generate successor candidates
        // tuple = tutor-course-shift tuple
        for (Tuple tuple : node.getTuples()) {
            // Skip over ghost tutors - TODO make this better
            if (tuple.getTutor().getId() == -1) {
                continue;
            }
            Node child = new Node(node);
            child.removeTuple(tuple);

            // Only candidate if not explored
            if (explored.contains(child)) {
                continue;
            }

            // Don't add more expensive children
            if (child.generateCost() > parentCost) {
                continue;
            }

            successorCandidates.add(child);
        }

        // Only return sufficient successor candidates
        for (Node candidate : successorCandidates) {
            HashMap<Course, Set<Shift>> coursesScheduled = new HashMap<>();

            for (Tuple tuple : candidate.getTuples()) {
                Tutor tutor = tuple.getTutor();
                Course course = tuple.getCourse();
                Shift shift = tuple.getShift();

                if (!coursesScheduled.containsKey(course)) {
                    coursesScheduled.put(course, new HashSet<>());
                }
                coursesScheduled.get(course).add(shift);
            }

            // All courses must be scheduled - hidden constraint
            if (!coursesScheduled.keySet().containsAll(coursesFromInput)) {
                continue;
            }

            boolean broken = false;

            // Course-Required Shift constraints
            for (Map.Entry<Course, Set<Shift>> entry : coursesScheduled.entrySet()) {
                Set<Shift> requiredShifts = entry.getKey().getCourseRequirements().getRequiredShifts();
                Set<Shift> scheduledShifts = entry.getValue();

                if (!requiredShifts.isEmpty()) {
                    // Use requiredShifts as constraint
                    if (!scheduledShifts.containsAll(requiredShifts)) {
                        broken = true;
                        break;
                    }
                } else {
                    // Use requiredShiftAmount as constraint
                    if (entry.getKey().getCourseRequirements().getRequiredShiftAmount() > scheduledShifts.size()) {
                        broken = true;
                        break;
                    }
                }
            }

            if (!broken) {
                successors.add(candidate);
            }
        }

        // TODO can remove redundant successorCandidates -> successors operation, but left here for readability
        return successors;
    }

    private static int generateCost(Node node) {
        return node.generateCost();
    }

    /**
     * Generate "max" node, with all tutors schedules to all possible courses in all viable shifts for those courses.
     * @return Node - root node, representing "max" node
     */
    private static Node generateRoot(List<Tutor> tutors, List<Course> courses, List<Shift> shifts) {
        // Generate max node
        Node root = new Node();

        for (Tutor tutor : tutors) {
            // TODO move this elsewhere? Only keep active tutors on fetch?
            if (!tutor.getTutorStatus().equals(TutorStatus.ACTIVE)) {
                continue;
            }

            Set<Course> possibleCourses = tutor.findPossibleCourses();
            Set<Shift> possibleShifts = tutor.findPossibleShifts();

            for (Course possibleCourse : possibleCourses) {
                if (courses.contains(possibleCourse)) {
                    for (Shift possibleShift : possibleShifts) {
                        if (shifts.contains(possibleShift)) {
                            Tuple tuple = new Tuple(tutor, possibleCourse, possibleShift);
                            root.addTuple(tuple);
                        }
                    }
                }
            }
        }

        return root;
    }

    private static Node generateRootWithGhostTutor(List<Tutor> tutors, List<Course> courses, List<Shift> shifts) {
        HashMap<Rank, Set<Course>> ghostCoursePreferences = new HashMap<>();
        ghostCoursePreferences.put(Rank.PREFER, new HashSet<>(courses));

        HashMap<Rank, Set<Shift>> ghostShiftPreferences = new HashMap<>();
        ghostShiftPreferences.put(Rank.PREFER, new HashSet<>(shifts));

        HashMap<Rank, Integer> ghostShiftFrequencyPreferences = new HashMap<>();
        ghostShiftFrequencyPreferences.put(Rank.PREFER, Integer.MAX_VALUE);

        TutorPreferences ghostTutorPreference = new TutorPreferences(ghostCoursePreferences, ghostShiftPreferences, ghostShiftFrequencyPreferences);
        Tutor ghostTutor = new Tutor(-1, "Ghost", "Tutor", ghostTutorPreference, TutorStatus.ACTIVE);

        tutors.add(ghostTutor);
        return generateRoot(tutors, courses, shifts);
    }

    private static Schedule scheduleFromNode(Node node) {
        Set<ScheduledShift> scheduledShifts = new TreeSet<>(Comparator.comparingInt(ScheduledShift::getShiftId));
        HashMap<Shift, HashMap<Tutor, Assignment>> shiftToTutorToAssignment = new HashMap<>();

        // Make map
        for (Tuple tuple : node.getTuples()) {
            Tutor tutor = tuple.getTutor();
            Course course = tuple.getCourse();
            Shift shift = tuple.getShift();

            if (!shiftToTutorToAssignment.containsKey(shift)) {
                shiftToTutorToAssignment.put(shift, new HashMap<>());
            }
            if (!shiftToTutorToAssignment.get(shift).containsKey(tutor)) {
                shiftToTutorToAssignment.get(shift).put(tutor, new Assignment(tutor));
            }

            shiftToTutorToAssignment.get(shift).get(tutor).addCourse(course);
        }

        // Form ScheduledShifts
        for (Shift shift : shiftToTutorToAssignment.keySet()) {
            HashMap<Tutor, Assignment> tutorToAssignment = shiftToTutorToAssignment.get(shift);

            ScheduledShift scheduledShift = new ScheduledShift(shift);
            for (Assignment assignment : tutorToAssignment.values()) {
                scheduledShift.addAssignment(assignment);
            }

            scheduledShifts.add(scheduledShift);
        }

        return new Schedule(scheduledShifts);
    }


    public static void main(String[] args) {
        List<Tutor> tutors;
        List<Course> courses;
        List<Shift> shifts;

        boolean useReal = true;

        if (useReal) {
            // Note: this file is not committed and should not be. If you don't make this file yourself (i.e. copy this from the ULC, you will have problems).
            // Don't commit it though!
            InputStream inputStream = Schedule.class.getClassLoader().getResourceAsStream("ulcrs-requirements.json");
            String response = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));

            // Make response
            JsonObject obj = new Gson().fromJson(response, JsonObject.class);
            ParsedULCResponse parse = DataParse.parse(obj);
            tutors = parse.getTutors();
            courses = parse.getCourses();
            shifts = parse.getShifts();
        } else {
            // Tutor
            InputStream is = DataStore.class.getClassLoader().getResourceAsStream("mockTutors_Full.json");
            JsonReader reader = new JsonReader(new InputStreamReader(is));
            tutors = new Gson().fromJson(reader, new TypeToken<List<Tutor>>() {}.getType());

            // Course
            is = DataStore.class.getClassLoader().getResourceAsStream("mockCourses_Full.json");
            reader = new JsonReader(new InputStreamReader(is));
            courses = new Gson().fromJson(reader, new TypeToken<List<Course>>() {}.getType());

            // Shift
            is = DataStore.class.getClassLoader().getResourceAsStream("mockShifts_Full.json");
            reader = new JsonReader(new InputStreamReader(is));
            shifts = new Gson().fromJson(reader, new TypeToken<List<Shift>>() {}.getType());
        }


        LocalDateTime start = LocalDateTime.now();
        List<Schedule> generatedSchedules = schedule(tutors, courses, shifts);
        LocalDateTime end = LocalDateTime.now();
    }

}
