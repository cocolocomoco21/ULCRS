package ulcrs.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger log = LoggerFactory.getLogger(DataStore.class);
    private static DataStore dataStore;
    private List<Tutor> tutors;
    private List<Course> courses;
    private List<Shift> shifts;
    private LocalDateTime timeFetched = LocalDateTime.MIN;

    private DataStore() {
        this.courses = new ArrayList<>();
        this.shifts = new ArrayList<>();
        this.tutors = new ArrayList<>();
    }

    private static DataStore getInstance() {
        if (dataStore == null) {
            dataStore = new DataStore();
        }
        return dataStore;
    }

    /**
     * Get the tutor of the specified id
     *
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
     *
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
     *
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
     *
     * @return List<Tutor> - list of all tutors
     */
    public static List<Tutor> getTutors(String cookie) {
        fetchIfRequired(getInstance().tutors, cookie);
        return getInstance().tutors;
    }

    /**
     * Get all courses, fetching from ULC if necessary.
     *
     * @return List<Course> - list of all courses
     */
    public static List<Course> getCourses(String cookie) {
        fetchIfRequired(getInstance().courses, cookie);
        return getInstance().courses;
    }

    /**
     * Get all shifts, fetching from ULC if necessary.
     *
     * @return List<Shift> - list of all shifts
     */
    public static List<Shift> getShifts(String cookie) {
        fetchIfRequired(getInstance().shifts, cookie);
        return getInstance().shifts;
    }

    /**
     * Check if data is cached (i.e. has already been fetched. If not, fetch from the ULC server.
     *
     * @param reference - reference being checked if it has been fetched
     * @param <T>       - generic type for reference
     */
    private static <T> void fetchIfRequired(List<T> reference, String cookie) {
        if (!isCached(reference)) {
            List<String> response = DataFetch.fetchFromULCServer(cookie);

            // TODO error handling if populateData() fails
            boolean result = populateData(response);
            if (!result) {
                log.info("Failed to populate (fetch and update) data");
            }
        }
    }

    /**
     * Check if the reference is cached. If null or an empty array, it has not been cached.
     *
     * @param reference - reference being checked for if it is cached
     * @param <T>       - generic type for reference
     * @return boolean - return if reference has been cached or not. If no, reference is null or empty array.
     */
    private static <T> boolean isCached(List<T> reference) {
        return reference != null && reference.size() > 0
                && getInstance().timeFetched != null;
    }

    /**
     * Populates and "caches" the data fetched from the ULC server. This parses the response data, which is saved as
     * JSON, and updates the data saved in the DataStore instance. As such, it "populates" the data.
     *
     * @param response List<String> - response from ULC server
     * @return boolean - returns success of parsing and saving the data
     */
    static boolean populateData(List<String> response) {
        // Expect response with only one line of JSON
        if (response.size() != 1) {
            log.info("Request failed because of invalid response", response);
            return false;
        }

        // Get response string into JsonObject
        String responseString = response.get(0);
        JsonObject obj = new Gson().fromJson(responseString, JsonObject.class);

        // Parse data
        ParsedULCResponse parsedResponse = DataParse.parse(obj);

        // Update DataStore's fields
        getInstance().courses = parsedResponse.getCourses();
        getInstance().shifts = parsedResponse.getShifts();
        getInstance().tutors = parsedResponse.getTutors();

        getInstance().timeFetched = LocalDateTime.now();

        return true;
    }

    /**
     * Fetch local mock data and update the data saved in the DataStore instance. This "caches" the data that is fetched.
     * <p>
     * TODO delete - kept here to provide easy way for frontend to use the simple dataset they've originally developed with
     */
    private static void fetchLocalMockData() {
        // Tutor
        InputStream is = DataStore.class.getClassLoader().getResourceAsStream("mockTutors_Full.json");
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        getInstance().tutors = new Gson().fromJson(reader, new TypeToken<List<Tutor>>() {
        }.getType());

        // Course
        is = DataStore.class.getClassLoader().getResourceAsStream("mockCourses_Full.json");
        reader = new JsonReader(new InputStreamReader(is));
        getInstance().courses = new Gson().fromJson(reader, new TypeToken<List<Course>>() {
        }.getType());

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
