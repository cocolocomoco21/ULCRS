package ulcrs.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ulcrs.models.course.Course;
import ulcrs.models.course.CourseIntensity;
import ulcrs.models.course.CourseRequirements;
import ulcrs.models.rank.Rank;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;
import ulcrs.models.tutor.TutorPreferences;
import ulcrs.models.tutor.TutorStatus;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DataParse {

    // Json keys for objects from ULC response
    private final static String COURSES_KEY = "courses";
    private final static String SHIFTS_KEY = "shifts";
    private final static String TUTORS_KEY = "tutors";

    // Json keys for specific objects (tutor, course, shift) and their attributes
    private final static String ID_KEY = "id";
    private final static String NAME_KEY = "name";
    private final static String DAY_KEY = "day";
    private final static String START_TIME_KEY = "startTime";
    private final static String END_TIME_KEY = "endTime";
    private final static String FIRST_NAME_KEY = "firstName";
    private final static String LAST_NAME_KEY = "lastName";
    private final static String PREF_COURSES_KEY = "prefCourses";
    private final static String WILLING_COURSES_KEY = "willingCourses";
    private final static String PREF_SHIFTS_KEY = "prefShift";
    private final static String WILLING_SHIFTS_KEY = "willingShift";
    private final static String PREF_SHIFT_AMOUNT_KEY = "prefShiftAmount";
    private final static String WILLING_SHIFT_AMOUNT_KEY = "willingShiftAmount";
    private final static String TUTOR_ROLE_KEY = "role";

    /**
     * Parse shifts, then courses, then tutors, in that order, from Json received from ULC.
     * Courses depend on shifts, and tutors depend on shifts and courses (which depend on shifts), so to reduce
     * redundant parsing, this "shift -> course -> tutor" order should be enforced.
     *
     * @param input - JsonObject response from ULC to parse into ULCRS data.
     * @return ParsedULCResponse - parsed response Json received from ULC.
     */
    static ParsedULCResponse parse(JsonObject input) {
        ParsedULCResponse parsed = new ParsedULCResponse();

        // Shifts
        HashMap<Integer, Shift> shifts = parseShifts(input);
        parsed.setShifts(new ArrayList<>(shifts.values()));

        // Courses
        HashMap<Integer, Course> courses = parseCourses(input, shifts);
        parsed.setCourses(new ArrayList<>(courses.values()));

        // Tutors
        HashMap<Integer, Tutor> tutors = parseTutors(input, courses, shifts);
        parsed.setTutors(new ArrayList<>(tutors.values()));

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

        // Transform json shifts into Shift objects
        HashMap<Integer, Shift> shifts = new HashMap<>();
        shiftsJson.forEach(element -> {
            JsonObject json = element.getAsJsonObject();

            // id
            String idStr = json.get(ID_KEY).getAsString();
            int id = Integer.valueOf(idStr);

            // day
            String dayStr = json.get(DAY_KEY).getAsString();
            DayOfWeek day = DayOfWeek.valueOf(dayStr);

            // startTime
            String startTimeStr = json.get(START_TIME_KEY).getAsString();
            LocalTime startTime = LocalTime.parse(startTimeStr);

            // endTime
            String endTimeStr = json.get(END_TIME_KEY).getAsString();
            LocalTime endTime = LocalTime.parse(endTimeStr);

            Shift shift = new Shift(id, day, startTime, endTime);
            shifts.put(id, shift);
        });

        return shifts;
    }

    /**
     * Parse courses. This depends on shifts.
     *
     * @param input - JsonObject response from ULC to parse into ULCRS data.
     * @param shifts - HashMap of shifts for easy lookup for already-parsed shifts.
     * @return HashMap - return HashMap of id:course for courses parsed from input.
     */
    private static HashMap<Integer, Course> parseCourses(JsonObject input, HashMap<Integer, Shift> shifts) {
        // Get course JsonArray from JsonObject
        JsonArray coursesJson = input.get(COURSES_KEY).getAsJsonArray();

        // Transform json courses into Course objects
        HashMap<Integer, Course> courses = new HashMap<>();
        coursesJson.forEach(element -> {
            JsonObject json = element.getAsJsonObject();

            // id
            String idStr = json.get(ID_KEY).getAsString();
            int id = Integer.valueOf(idStr);

            // name
            String name = json.get(NAME_KEY).getAsString();

            // TODO courseRequirements
            // This will require the shifts list passed in to refer to existing shifts

            // TODO hack to make things work for Iteration 2. Make better
            Course course = new Course(id, name, new CourseRequirements(Collections.emptySet(), 0, 0, CourseIntensity.MEDIUM));
            courses.put(id, course);
        });

        return courses;
    }

    /**
     * Parse tutors. This depends on courses and shifts.
     *
     * @param input - JsonObject response from ULC to parse into ULCRS data.
     * @param courses - HashMap of courses for easy lookup for already-parsed courses.
     * @param shifts - HashMap of shifts for easy lookup for already-parsed shifts.
     * @return HashMap - return HashMap of id:tutor for tutors parsed from input.
     */
    private static HashMap<Integer, Tutor> parseTutors(JsonObject input, HashMap<Integer, Course> courses, HashMap<Integer, Shift> shifts) {
        // Get tutor JsonArray from JsonObject
        JsonArray tutorsJson = input.get(TUTORS_KEY).getAsJsonArray();

        // Transform json tutors into Tutor objects
        HashMap<Integer, Tutor> tutors = new HashMap<>();
        tutorsJson.forEach(element -> {
            JsonObject json = element.getAsJsonObject();

            // id
            String idStr = json.get(ID_KEY).getAsString();
            int id = Integer.valueOf(idStr);

            // firstName
            String firstName = json.get(FIRST_NAME_KEY).getAsString();

            // lastName
            String lastName = json.get(LAST_NAME_KEY).getAsString();

            // Shift preference
            HashMap<Rank, Set<Shift>> shiftPreferences = new HashMap<>();
            JsonArray prefShiftJson = json.get(PREF_SHIFTS_KEY).getAsJsonArray();
            JsonArray willingShiftJson = json.get(WILLING_SHIFTS_KEY).getAsJsonArray();
            if (prefShiftJson.size() == 0 && willingShiftJson.size() == 0) {
                // Bad data, do not add (filter it out)
                return;
            }

            HashSet<Shift> preferredShifts = new HashSet<>();
            prefShiftJson.forEach(prefShift-> {
                int shiftId = prefShift.getAsInt();
                Shift shift = shifts.get(shiftId);

                // Skip over non-extistent shifts
                if (shift != null) {
                    preferredShifts.add(shift);
                }
            });

            HashSet<Shift> willingShifts = new HashSet<>();
            willingShiftJson.forEach(willingShift -> {
                int shiftId = willingShift.getAsInt();
                Shift shift = shifts.get(shiftId);

                // Skip over non-existent shifts
                if (shift != null) {
                    willingShifts.add(shift);
                }
            });

            shiftPreferences.put(Rank.PREFER, preferredShifts);
            shiftPreferences.put(Rank.WILLING, willingShifts);

            // Course preference
            HashMap<Rank, Set<Course>> coursePreferences = new HashMap<>();
            JsonArray prefCourseJson = json.get(PREF_COURSES_KEY).getAsJsonArray();
            JsonArray willingCourseJson = json.get(WILLING_COURSES_KEY).getAsJsonArray();
            if (prefCourseJson.size() == 0 && willingCourseJson.size() == 0) {
                // Bad data, do not add (filter it out)
                return;
            }

            HashSet<Course> preferredCourses = new HashSet<>();
            prefCourseJson.forEach(prefCourse -> {
                int courseId = prefCourse.getAsInt();
                Course course = courses.get(courseId);

                // Skip over non-extistent courses
                if (course != null) {
                    preferredCourses.add(course);
                }
            });

            HashSet<Course> willingCourses = new HashSet<>();
            willingCourseJson.forEach(willingCourse -> {
                int courseId = willingCourse.getAsInt();
                Course course = courses.get(courseId);

                // Skip over non-extistent courses
                if (course != null) {
                    willingCourses.add(course);
                }
            });

            coursePreferences.put(Rank.PREFER, preferredCourses);
            coursePreferences.put(Rank.WILLING, willingCourses);

            // Shift frequency preference
            HashMap<Rank, Integer> shiftFrequencyPreference = new HashMap<>();
            JsonElement prefShiftAmountJson = json.get(PREF_SHIFT_AMOUNT_KEY);
            JsonElement willingShiftAmountJson = json.get(WILLING_SHIFT_AMOUNT_KEY);
            if (prefShiftAmountJson.isJsonNull() && willingShiftAmountJson.isJsonNull()) {
                // Bad data, do not add (filter it out)
                return;
            }

            Integer prefShiftAmount = prefShiftAmountJson.isJsonNull() ? null : prefShiftAmountJson.getAsInt();
            Integer willingShiftAmount = willingShiftAmountJson.isJsonNull() ? null : willingShiftAmountJson.getAsInt();
            shiftFrequencyPreference.put(Rank.PREFER, prefShiftAmount);
            shiftFrequencyPreference.put(Rank.WILLING, willingShiftAmount);

            // Tutor preference
            TutorPreferences tutorPreferences = new TutorPreferences(coursePreferences, shiftPreferences, shiftFrequencyPreference);

            // TutorStatus
            String tutorRole = json.get(TUTOR_ROLE_KEY).getAsString();
            TutorStatus tutorStatus = TutorStatus.fromULCRole(tutorRole);
            if (tutorStatus == null) {
                // Bad data, do not add (filter it out)
                return;
            }

            Tutor tutor = new Tutor(id, firstName, lastName, tutorPreferences, tutorStatus);
            tutors.put(id, tutor);
        });

        return tutors;
    }
}
