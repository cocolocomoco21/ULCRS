package ulcrs.models.schedule;

import ulcrs.models.course.Course;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Node {

    private List<Tuple> tuples;
    private Integer cost;

    public Node() {
        this.tuples = new ArrayList<>();
    }

    public Node(Node node) {
        this();
        for (Tuple tuple : node.getTuples()){
            this.tuples.add(tuple);
        }
    }

    public void addTuple(Tuple tuple) {
        this.tuples.add(tuple);
    }

    public List<Tuple> getTuples() {
        return this.tuples;
    }

    public void removeTuple(Tuple tuple) {
        tuples.remove(tuple);
    }

    public int getCost() {
        if (cost == null) {
            generateCost();
        }

        return this.cost;
    }

    public int generateCost() {
        int numViolatedConstraints = 0;

        // Object -> id maps
        HashMap<Course, Set<Integer>> courseOnShifts = new HashMap<>();
        HashMap<Tutor, Integer> tutorToNumShifts = new HashMap<>();
        HashMap<Shift, HashMap<Tutor, Set<Course>>> shiftToTutorCourses = new HashMap<>();

        List<Tuple> tuples = this.tuples;
        for (Tuple tuple : tuples) {
            Tutor tutor = tuple.getTutor();
            Course course = tuple.getCourse();
            Shift shift = tuple.getShift();

            // Don't count violated constraints on ghost tutors - TODO make better
            if (tutor.getId() == -1) {
                continue;
            }

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
            courseOnShifts.put(course, scheduledCourses);

            // Add to tutor -> numShifts map
            Integer currNumShifts = tutorToNumShifts.get(tutor);
            if (currNumShifts == null) {
                currNumShifts = 0;
            }
            currNumShifts++;
            tutorToNumShifts.put(tutor, currNumShifts);

            // Add to shift -> {tutor -> courses} map
            if (!shiftToTutorCourses.containsKey(shift)) {
                shiftToTutorCourses.put(shift, new HashMap<>());
            }
            if (!shiftToTutorCourses.get(shift).containsKey(tutor)) {
                shiftToTutorCourses.get(shift).put(tutor, new HashSet<>());
            }
            Set<Course> coursesForTutorOnShift = shiftToTutorCourses.get(shift).get(tutor);
            coursesForTutorOnShift.add(course);
            shiftToTutorCourses.get(shift).put(tutor, coursesForTutorOnShift);
        }

        for (Course course : courseOnShifts.keySet()) {
            Set<Shift> requiredShifts = course.getCourseRequirements().getRequiredShifts();
            Set<Integer> scheduledShifts = courseOnShifts.get(course);

            // Course-Required Shift constraint
            for (Shift requiredShift : requiredShifts) {
                if (!scheduledShifts.contains(requiredShift.getId())) {
                    numViolatedConstraints++;
                }
            }

            // Course-Required Shift amount constraint
            if (course.getCourseRequirements().getRequiredShiftAmount() > scheduledShifts.size()) {
                int additionalRequiredShiftAmount = course.getCourseRequirements().getRequiredShiftAmount() - scheduledShifts.size();
                numViolatedConstraints += additionalRequiredShiftAmount;
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

        /*
        // Course intensity constraint
        for (Shift shift : shiftToTutorCourses.keySet()) {
            HashMap<Tutor, Set<Course>> tutorsToCourses = shiftToTutorCourses.get(shift);
            for (Tutor tutor : tutorsToCourses.keySet()) {
                Set<Course> coursesForTutor = tutorsToCourses.get(tutor);
                int intensityCount = 0;
                for (Course course : coursesForTutor) {
                    intensityCount += course.getCourseRequirements().getIntensity().toWeightedValue();
                }

                if (intensityCount > 6) {
                    numViolatedConstraints += intensityCount - 6;
                }
            }
        }
        */

        this.cost = numViolatedConstraints;
        return numViolatedConstraints;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(tuples, node.tuples);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tuples);
    }
}
