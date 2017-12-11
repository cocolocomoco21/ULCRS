package ulcrs.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import ulcrs.models.course.Course;
import ulcrs.models.course.CourseRequirements;
import ulcrs.models.rank.Rank;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;
import ulcrs.models.tutor.TutorPreferences;
import ulcrs.models.ulc.ULCCourse;
import ulcrs.models.ulc.ULCCourseRequirements;
import ulcrs.models.ulc.ULCShift;
import ulcrs.models.ulc.ULCTutor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DataParse {

    // Json keys for objects from ULC response
    private final static String COURSES_KEY = "courses";
    private final static String COURSE_REQUIREMENTS_KEY = "requirements";
    private final static String SHIFTS_KEY = "shifts";
    private final static String TUTORS_KEY = "tutors";

    /**
     * Parse shifts, then courses, then tutors, in that order, from Json received from ULC.
     * Courses depend on shifts, and tutors depend on shifts and courses (which depend on shifts), so to reduce
     * redundant parsing, this "shift -> course -> tutor" order should be enforced.
     *
     * @param input - JsonObject response from ULC to parse into ULCRS data.
     * @return ParsedULCResponse - parsed response Json received from ULC.
     */
    public static ParsedULCResponse parse(JsonObject input) {
        ParsedULCResponse parsed = new ParsedULCResponse();

        // Shifts
        HashMap<Integer, Shift> shifts = parseShifts(input);
        ArrayList<Shift> shiftsList = new ArrayList<>(shifts.values());
        shiftsList.sort(Comparator.comparingInt(Shift::getId));
        parsed.setShifts(shiftsList);

        // Courses
        HashMap<Integer, Course> courses = parseCourses(input, shifts);
        ArrayList<Course> coursesList = new ArrayList<>(courses.values());
        coursesList.sort(Comparator.comparingInt(Course::getId));
        parsed.setCourses(coursesList);

        // Tutors
        HashMap<Integer, Tutor> tutors = parseTutors(input, shifts, courses);
        ArrayList<Tutor> tutorsList = new ArrayList<>(tutors.values());
        tutorsList.sort(Comparator.comparingInt(Tutor::getId));
        parsed.setTutors(new ArrayList<>(tutorsList));

        return filter(parsed);
    }

    private static ParsedULCResponse filter(ParsedULCResponse parsed) {
        // Tutors
        List<Tutor> tutors = parsed.getTutors();
        List<Tutor> tutorsFiltered = new ArrayList<>();
        for (Tutor tutor : tutors) {
            TutorPreferences preferences = tutor.getTutorPreferences();

            // Tutor does not prefer any courses
            Set<Course> prefCourse = preferences.getCoursePreferences().get(Rank.PREFER);
            if (prefCourse == null || prefCourse.isEmpty()) {
                continue;
            }

            // Tutor does not prefer any shifts
            Set<Shift> prefShift = preferences.getShiftPreferences().get(Rank.PREFER);
            if (prefShift == null || prefShift.isEmpty()) {
                continue;
            }

            // Tutor has empty shift frequency preferences
            Integer prefShiftAmount = preferences.getShiftFrequencyPreferences().get(Rank.PREFER);
            Integer willingShiftAmount = preferences.getShiftFrequencyPreferences().get(Rank.WILLING);
            if (prefShiftAmount == null || willingShiftAmount == null ||
                    (prefShiftAmount == 0 && willingShiftAmount == 0)) {
                continue;
            }

            tutorsFiltered.add(tutor);
        }
        parsed.setTutors(tutorsFiltered);

        // Courses
        List<Course> courses = parsed.getCourses();
        List<Course> coursesFiltered = new ArrayList<>();
        for (Course course : courses) {
            CourseRequirements requirements = course.getCourseRequirements();

            // Course has empty specifics AND requiredShiftAmount and numTutors from ULC
            Map<Shift, Integer> numTutorsPerShift = requirements.getNumTutorsPerShift();
            int requiredShiftAmount = requirements.getRequiredShiftAmount();
            int numTutorsPerWeek = requirements.getNumTutorsPerWeek();
            if (numTutorsPerShift == null || numTutorsPerShift.isEmpty() &&
                    (requiredShiftAmount == 0 && numTutorsPerWeek == 0)) {
                continue;
            }

            coursesFiltered.add(course);
        }
        parsed.setCourses(coursesFiltered);

        return parsed;
    }

    /**
     * Parse shifts.
     *
     * @param input - JsonObject response from ULC to parse into ULCRS data.
     * @return HashMap - return HashMap of id:shift for shifts parsed from input.
     */
    private static HashMap<Integer, Shift> parseShifts(JsonObject input) {
        // Get shift JsonArray from JsonObject
        JsonArray shiftsJson = input.get(SHIFTS_KEY).getAsJsonArray();

        List<ULCShift> ulcShifts = new Gson().fromJson(shiftsJson, new TypeToken<List<ULCShift>>() {
        }.getType());

        // Transform json shifts into Shift objects
        HashMap<Integer, Shift> shifts = new HashMap<>();
        ulcShifts.forEach(ulcShift -> {
            Shift shift = ulcShift.toShift();
            shifts.put(shift.getId(), shift);
        });

        return shifts;
    }

    /**
     * Parse courses. This depends on shifts.
     *
     * @param input  - JsonObject response from ULC to parse into ULCRS data.
     * @param shifts - HashMap of shifts for easy lookup for already-parsed shifts.
     * @return HashMap - return HashMap of id:course for courses parsed from input.
     */
    private static HashMap<Integer, Course> parseCourses(JsonObject input, HashMap<Integer, Shift> shifts) {
        // Get course JsonArray from JsonObject
        JsonArray coursesJson = input.get(COURSES_KEY).getAsJsonArray();
        JsonArray courseRequirementsJson = input.get(COURSE_REQUIREMENTS_KEY).getAsJsonArray();

        List<ULCCourse> ulcCourses = new Gson().fromJson(coursesJson, new TypeToken<List<ULCCourse>>() {
        }.getType());
        List<ULCCourseRequirements> ulcCourseRequirementsList = new Gson().fromJson(courseRequirementsJson, new TypeToken<List<ULCCourseRequirements>>() {
        }.getType());

        // Map course id to ULCCourseRequirements object for that course
        Map<Integer, ULCCourseRequirements> courseIdToULCCourseRequirements = ulcCourseRequirementsList.stream()
                .collect(Collectors.toMap(ULCCourseRequirements::getCourseId, item -> item));

        // Transform json courses into Course objects
        HashMap<Integer, Course> courses = new HashMap<>();
        ulcCourses.forEach(ulcCourse -> {
            ULCCourseRequirements ulcCourseRequirement = courseIdToULCCourseRequirements.get(ulcCourse.getId());
            if (ulcCourseRequirement != null) {
                CourseRequirements courseRequirement = ulcCourseRequirement.toCourseRequirements(shifts);
                Course course = ulcCourse.toCourse(courseRequirement);
                courses.put(course.getId(), course);
            }
        });

        return courses;
    }

    /**
     * Parse tutors. This depends on courses and shifts.
     *
     * @param input   - JsonObject response from ULC to parse into ULCRS data.
     * @param courses - HashMap of courses for easy lookup for already-parsed courses.
     * @param shifts  - HashMap of shifts for easy lookup for already-parsed shifts.
     * @return HashMap - return HashMap of id:tutor for tutors parsed from input.
     */
    private static HashMap<Integer, Tutor> parseTutors(JsonObject input, HashMap<Integer, Shift> shifts, HashMap<Integer, Course> courses) {
        // Get tutor JsonArray from JsonObject
        JsonArray tutorsJson = input.get(TUTORS_KEY).getAsJsonArray();

        List<ULCTutor> ulcTutors = new Gson().fromJson(tutorsJson, new TypeToken<List<ULCTutor>>() {
        }.getType());

        // Transform json tutors into Tutor objects
        HashMap<Integer, Tutor> tutors = new HashMap<>();
        ulcTutors.forEach(ulcTutor -> {
            Tutor tutor = ulcTutor.toTutor(shifts, courses);
            tutors.put(tutor.getId(), tutor);
        });

        return tutors;
    }
}
