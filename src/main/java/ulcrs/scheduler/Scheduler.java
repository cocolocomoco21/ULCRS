package ulcrs.scheduler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulcrs.data.DataParse;
import ulcrs.data.DataStore;
import ulcrs.data.ParsedULCResponse;
import ulcrs.models.course.Course;
import ulcrs.models.schedule.Node;
import ulcrs.models.schedule.Schedule;
import ulcrs.models.schedule.Tuple;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;
import ulcrs.models.tutor.TutorStatus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents the scheduling algorithm.
 */
public class Scheduler {
    private static Logger log = LoggerFactory.getLogger(Scheduler.class);

    private static List<Schedule> generatedSchedules;

    protected static List<Schedule> schedule(List<Tutor> tutors, List<Course> courses, List<Shift> shifts) {
        // Start scheduling algorithm
        int desiredCost = 0;
        int desiredCostLimit = 10;
        Node root = generateRoot(tutors, courses, shifts);
        Node result = null;

        while (result == null && desiredCost <= desiredCostLimit) {
            result = search(root, desiredCost++);
        }

        // Finish scheduling algorithm
        // TODO turn node into list of schedules
        generatedSchedules = null;
        return generatedSchedules;
    }

    private static Node search (Node inputNode, int desiredCost) {
        List<Node> frontier = new ArrayList<>();
        frontier.add(inputNode);
        //List<Node> explored = Collections.emptyList();

        while (frontier.size() != 0) {
            Node node = frontier.remove(0);
            //explored.add(node);

            List<Node> successors = successors(node);
            for (Node successor : successors) {
                int cost = generateCost(successor);
                if (cost < desiredCost) {
                    return successor;
                }

                if (!frontier.contains(successor)) {
                    frontier.add(successor);
                }
            }
        }

        return null;
    }

    private static List<Node> successors(Node node) {
        List<Node> successorCandidates = new ArrayList<>();
        List<Node> successors = new ArrayList<>();

        // generate successor candidates
        // tuple = tutor-course-shift tuple
        for (Tuple tuple : node.getTuples()) {
            Node child = new Node(node);
            child.removeTuple(tuple);
            successorCandidates.add(child);
        }

        // only return sufficient successor candidates
        for (Node candidate : successorCandidates) {
            for (Tuple tuple : candidate.getTuples()) {
                Tutor tutor = tuple.getTutor();
                Course course = tuple.getCourse();
                Shift shift = tuple.getShift();

                if (tutor.findPossibleCourses().contains(course) && tutor.findPossibleShifts().contains(shift)) {
                    successors.add(candidate);
                }
            }
        }

        // TODO can remove redundant successorCandidates -> successors operation, but left here for readability
        return successors;
    }

    private static int generateCost(Node node) {
        int numViolatedConstraints = 0;

        // Object -> id maps
        HashMap<Course, Set<Integer>> courseOnShifts = new HashMap<>();
        HashMap<Tutor, Integer> tutorToNumShifts = new HashMap<>();

        List<Tuple> tuples = node.getTuples();
        for (Tuple tuple : tuples) {
            Tutor tutor = tuple.getTutor();
            Course course = tuple.getCourse();
            Shift shift = tuple.getShift();

            // Tutor-Shift constraint
            if (!tutor.findPossibleShifts().contains(shift)) {
                numViolatedConstraints++;
            }

            // Tutor-Course constraint
            if (!tutor.findPossibleCourses().contains(course)) {
                numViolatedConstraints++;
            }

            // Add to course -> shifts map
            Set<Integer> scheduledCourses = courseOnShifts.get(course);
            if (scheduledCourses == null) {
                scheduledCourses = new HashSet<>();
            }
            scheduledCourses.add(shift.getId());

            // Add to tutor -> numShifts map
            Integer currNumShifts = tutorToNumShifts.get(tutor);
            currNumShifts++;
            tutorToNumShifts.put(tutor, currNumShifts);
        }

        for (Course course : courseOnShifts.keySet()) {
            Set<Shift> requiredShifts = course.getCourseRequirements().getRequiredShifts();
            Set<Integer> scheduledShifts = courseOnShifts.get(course);

            for (Shift requiredShift : requiredShifts) {
                if (!scheduledShifts.contains(requiredShift.getId())) {
                    numViolatedConstraints++;
                }
            }
        }

        for (Tutor tutor : tutorToNumShifts.keySet()) {
            int numScheduledShifts = tutorToNumShifts.get(tutor);
            int possibleNumShifts = tutor.findPossibleShiftFrequency();

            if (numScheduledShifts > possibleNumShifts) {
                int numExtraShifts = numScheduledShifts - possibleNumShifts;
                numViolatedConstraints += numExtraShifts;
            }
        }

        return numViolatedConstraints;
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

            for (Course course : courses) {
                for (Shift shift : shifts) {
                    if (course.getCourseRequirements().getRequiredShifts().contains(shift)) {
                        Tuple tuple = new Tuple(tutor, course, shift);
                        root.addTuple(tuple);
                    }
                }
            }
        }

        return root;
    }


    public static void main(String[] args) {
        InputStream inputStream = DataStore.class.getClassLoader().getResourceAsStream("mockULCResponse.json");
        String response = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));

        // Make response
        JsonObject obj = new Gson().fromJson(response, JsonObject.class);

        ParsedULCResponse parse = DataParse.parse(obj);

        List<Schedule> generatedSchedules = schedule(parse.getTutors(), parse.getCourses(), parse.getShifts());

        System.out.println(generatedSchedules);
    }

}
