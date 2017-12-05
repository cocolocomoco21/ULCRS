package ulcrs.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import ulcrs.models.course.Course;
import ulcrs.models.course.CourseIntensity;
import ulcrs.models.course.CourseRequirements;
import ulcrs.models.rank.Rank;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;
import ulcrs.models.tutor.TutorPreferences;
import ulcrs.models.tutor.TutorStatus;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataStore {

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

    private List<Tutor> tutors;
    private List<Course> courses;
    private List<Shift> shifts;
    private LocalDateTime timeFetched = LocalDateTime.MIN;

    private static DataStore dataStore;

    private static DataStore getInstance() {
        if (dataStore == null) {
            dataStore = new DataStore();
        }
        return dataStore;
    }

    private DataStore() {
        this.courses = new ArrayList<>();
        this.shifts = new ArrayList<>();
        this.tutors = new ArrayList<>();
    }


    /**
     * Get the tutor of the specified id
     * @param id - id of the tutor
     * @return Tutor - the Tutor with the specified id, if it exists. Otherwise, null.
     */
    public static Tutor getTutor(int id, String cookie) {
        fetchIfRequired(getInstance().tutors, cookie);
        return getInstance().tutors.stream()
                .filter(tutor -> tutor.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Get the course of the specified id
     * @param id - id of the course
     * @return Course - the Course with the specified id, if it exists. Otherwise, null.
     */
    public static Course getCourse(int id, String cookie) {
        fetchIfRequired(getInstance().courses, cookie);
        return getInstance().courses.stream()
                .filter(course -> course.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Get the shift of the specified id
     * @param id - id of the shift
     * @return Shift - the shift with the specified id, if it exists. Otherwise, null.
     */
    public static Shift getShift(int id, String cookie) {
        fetchIfRequired(getInstance().shifts, cookie);
        return getInstance().shifts.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all tutors, fetching from ULC if necessary.
     * @return List<Tutor> - list of all tutors
     */
    public static List<Tutor> getTutors(String cookie) {
        fetchIfRequired(getInstance().tutors, cookie);
        return getInstance().tutors;
    }

    /**
     * Get all courses, fetching from ULC if necessary.
     * @return List<Course> - list of all courses
     */
    public static List<Course> getCourses(String cookie) {
        fetchIfRequired(getInstance().courses, cookie);
        return getInstance().courses;
    }

    /**
     * Get all shifts, fetching from ULC if necessary.
     * @return List<Shift> - list of all shifts
     */
    public static List<Shift> getShifts(String cookie) {
        fetchIfRequired(getInstance().shifts, cookie);
        return getInstance().shifts;
    }

    /**
     * Check if data is cached (i.e. has already been fetched. If not, fetch from the ULC server.
     * @param reference - reference being checked if it has been fetched
     * @param <T> - generic type for reference
     */
    private static <T> void fetchIfRequired(List<T> reference, String cookie) {
        if (!isCached(reference)) {
            List<String> response = DataFetch.fetchFromULCServer(cookie);

            // TODO error handling if populateData() fails
            populateData(response);
        }
    }

    /**
     * Check if the reference is cached. If null or an empty array, it has not been cached.
     * @param reference - reference being checked for if it is cached
     * @param <T> - generic type for reference
     * @return boolean - return if reference has been cached or not. If no, reference is null or empty array.
     */
    private static <T> boolean isCached(List<T> reference) {
        return reference != null && reference.size() > 0
                && getInstance().timeFetched != null;
    }

    /**
     * Populates and "caches" the data fetched from the ULC server. This parses the response data, which is saved as
     * JSON, and updates the data saved in the DataStore instance. As such, it "populates" the data.
     * @param response List<String> - response from ULC server
     * @return boolean - returns success of parsing and saving the data
     */
    static boolean populateData(List<String> response) {
        // Expect response with only one line of JSON
        if (response.size() != 1) {
            return false;
        }

        // Get response string into JsonObject
        String responseString = response.get(0);
        JsonObject obj = new Gson().fromJson(responseString, JsonObject.class);

        // Get courses, shifts, and tutors JsonArrays from JsonObject
        JsonArray coursesJson = obj.get(COURSES_KEY).getAsJsonArray();
        JsonArray shiftsJson = obj.get(SHIFTS_KEY).getAsJsonArray();
        JsonArray tutorsJson = obj.get(TUTORS_KEY).getAsJsonArray();

        // TODO far better error catching for malformed json

        // Transform json shifts into Shift objects
        List<Shift> shifts = new ArrayList<>();
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
            shifts.add(shift);
        });

        // Transform json courses into Course objects
        List<Course> courses = new ArrayList<>();
        coursesJson.forEach(element -> {
            JsonObject json = element.getAsJsonObject();

            // id
            String idStr = json.get(ID_KEY).getAsString();
            int id = Integer.valueOf(idStr);

            // name
            String name = json.get(NAME_KEY).getAsString();

            // TODO courseRequirements

            // TODO hack to make things work for Iteration 2. Make better
            Course course = new Course(id, name, new CourseRequirements(Collections.emptySet(), 0, 0, CourseIntensity.MEDIUM));
            courses.add(course);
        });

        // Transform json tutors into Tutor objects
        List<Tutor> tutors = new ArrayList<>();
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
            if (prefShiftJson.size() == 0 || willingShiftJson.size() == 0) {
                // Bad data, filter it out
                // TODO continue
            }

            HashSet<Shift> preferredShifts = new HashSet<>();
            prefShiftJson.forEach(prefShift-> {
                int shiftId = prefShift.getAsInt();
                // TODO super inefficient, use HashMap on course id
                Shift shift = shifts.stream()
                        .filter(s -> s.getId() == shiftId)
                        .findFirst()
                        .orElse(null);
                if (shift == null) {
                    // TODO fail
                }

                preferredShifts.add(shift);
            });

            // Willing shift
            HashSet<Shift> willingShifts = new HashSet<>();
            willingShiftJson.forEach(willingShift -> {
                int shiftId = willingShift.getAsInt();
                // TODO super inefficient, use HashMap on course id
                Shift shift = shifts.stream()
                        .filter(s -> s.getId() == shiftId)
                        .findFirst()
                        .orElse(null);
                if (shift == null) {
                    // TODO fail
                }

                willingShifts.add(shift);
            });

            shiftPreferences.put(Rank.PREFER, preferredShifts);
            shiftPreferences.put(Rank.WILLING, willingShifts);

            // Course preference
            HashMap<Rank, Set<Course>> coursePreferences = new HashMap<>();

            // Preferred courses
            JsonArray prefCourseJson = json.get(PREF_COURSES_KEY).getAsJsonArray();
            JsonArray willingCourseJson = json.get(WILLING_COURSES_KEY).getAsJsonArray();
            if (prefCourseJson.size() == 0 || willingCourseJson.size() == 0) {
                // Bad data, filter it out
                // TODO continue
            }

            HashSet<Course> preferredCourses = new HashSet<>();
            prefCourseJson.forEach(prefCourse -> {
                int courseId = prefCourse.getAsInt();
                // TODO super inefficient, use HashMap on course id
                Course course = courses.stream()
                        .filter(c -> c.getId() == courseId)
                        .findFirst()
                        .orElse(null);
                if (course == null) {
                    // TODO fail
                }

                preferredCourses.add(course);
            });

            // Willing courses
            HashSet<Course> willingCourses = new HashSet<>();
            willingCourseJson.forEach(willingCourse -> {
                int courseId = willingCourse.getAsInt();
                // TODO super inefficient, use HashMap on course id
                Course course = courses.stream()
                        .filter(c -> c.getId() == courseId)
                        .findFirst()
                        .orElse(null);
                if (course == null) {
                    // TODO fail
                }

                willingCourses.add(course);
            });

            coursePreferences.put(Rank.PREFER, preferredCourses);
            coursePreferences.put(Rank.WILLING, willingCourses);

            // Shift frequency preference
            HashMap<Rank, Integer> shiftFrequencyPreference = new HashMap<>();

            JsonElement prefShiftAmountJson = json.get(PREF_SHIFT_AMOUNT_KEY);
            JsonElement willingShiftAmountJson = json.get(WILLING_SHIFT_AMOUNT_KEY);
            if (prefShiftAmountJson.isJsonNull() || willingShiftAmountJson.isJsonNull()) {
                // Bad data
                // TODO continue
            }

            Integer prefShiftAmount = prefShiftAmountJson.isJsonNull() ? null : prefShiftAmountJson.getAsInt();
            Integer willingShiftAmount = willingShiftAmountJson.isJsonNull() ? null : willingShiftAmountJson.getAsInt();
            shiftFrequencyPreference.put(Rank.PREFER, prefShiftAmount);
            shiftFrequencyPreference.put(Rank.WILLING, willingShiftAmount);

            // Tutor preference
            TutorPreferences tutorPreferences = new TutorPreferences(coursePreferences, shiftPreferences, shiftFrequencyPreference);

            // TODO TutorStatus

            Tutor tutor = new Tutor(id, firstName, lastName, tutorPreferences, TutorStatus.ACTIVE);
            tutors.add(tutor);
        });

        // Update DataStore's fields
        getInstance().courses = courses;
        getInstance().shifts = shifts;
        getInstance().tutors = tutors;

        getInstance().timeFetched = LocalDateTime.now();

        return true;
    }

    /**
     * Fetch local mock data and update the data saved in the DataStore instance. This "caches" the data that is fetched.
     *
     * TODO delete - kept here to provide easy way for frontend to use the simple dataset they've originally developed with
     */
    private static void fetchLocalMockData() {
        // Tutor
        InputStream is = DataStore.class.getClassLoader().getResourceAsStream("mockTutors_Full.json");
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        getInstance().tutors = new Gson().fromJson(reader, new TypeToken<List<Tutor>>() {}.getType());

        // Course
        is = DataStore.class.getClassLoader().getResourceAsStream("mockCourses_Full.json");
        reader = new JsonReader(new InputStreamReader(is));
        getInstance().courses = new Gson().fromJson(reader, new TypeToken<List<Course>>() {}.getType());

        // TODO using tutors and courses, get all shift information and save into getInstance().shifts
        // Shift
        LocalTime startTime = LocalTime.of(6, 30);
        LocalTime endTime = LocalTime.of(9, 0);

        getInstance().shifts = Arrays.asList(
                new Shift(0, DayOfWeek.SUNDAY, startTime, endTime),
                new Shift(1, DayOfWeek.MONDAY, startTime, endTime),
                new Shift(2, DayOfWeek.TUESDAY, startTime, endTime),
                new Shift(3, DayOfWeek.WEDNESDAY, startTime, endTime),
                new Shift(4, DayOfWeek.THURSDAY, startTime, endTime));

        getInstance().timeFetched = LocalDateTime.now();
    }
}
