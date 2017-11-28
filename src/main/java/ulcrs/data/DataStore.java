package ulcrs.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import ulcrs.models.course.Course;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        // TODO implement - requires getting shifts from course, tutor data
        fetchIfRequired(getInstance().shifts, cookie);
        return getInstance().shifts;
    }

    /**
     * Check if data is cached (i.e. has already been fetched. If not, fetch from the ULC server.
     * @param reference - reference being checked if it has been fetched
     * @param <T> - generic type for reference
     */
    private static <T> void fetchIfRequired(T reference, String cookie) {
        if (!isCached(reference)) {
            List<String> response = DataFetch.fetchFromULCServer(cookie);
            // TODO error handling if populateData() fails
            populateData(response);
        }
    }

    /**
     * Check if the reference is cached. If null, it has not been cached.
     * @param reference - reference being checked for if it is cached
     * @param <T> - generic type for reference
     * @return boolean - return if reference has been cached or not. If no, it is null.
     */
    private static <T> boolean isCached(T reference) {
        return reference != null;
    }


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

            Course course = new Course(id, name, null);
            courses.add(course);
        });

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

            // TODO tutorPreferences
            // TODO tutorStatus

            Tutor tutor = new Tutor(id, firstName, lastName, null, null);
            tutors.add(tutor);
        });

        getInstance().courses = courses;
        getInstance().shifts = shifts;
        getInstance().tutors = tutors;

        return true;
    }

    /**
     * Fetch data from the ULC server and update the data saved in the DataStore instance. This "caches" the
     * data that is fetched.
     */
    private static void fetch() {
        // TODO delete - kept here as reference to provide easy way for frontend to use simple dataset they've originally developed with
        // Fetch from ULC - TODO
        // For now, just get data from mock data

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
