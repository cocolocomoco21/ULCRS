package ulcrs.data;

import com.google.gson.Gson;
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
import java.util.Arrays;
import java.util.List;

public class DataStore {

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
    public static Tutor getTutor(int id) {
        fetchIfRequired(getInstance().tutors);
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
    public static Course getCourse(int id) {
        fetchIfRequired(getInstance().courses);
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
    public static Shift getShift(int id) {
        fetchIfRequired(getInstance().shifts);
    	return getInstance().shifts.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all tutors, fetching from ULC if necessary.
     * @return List<Tutor> - list of all tutors
     */
    public static List<Tutor> getTutors() {
        fetchIfRequired(getInstance().tutors);
        return getInstance().tutors;
    }

    /**
     * Get all courses, fetching from ULC if necessary.
     * @return List<Course> - list of all courses
     */
    public static List<Course> getCourses() {
        fetchIfRequired(getInstance().courses);
        return getInstance().courses;
    }

    /**
     * Get all shifts, fetching from ULC if necessary.
     * @return List<Shift> - list of all shifts
     */
    public static List<Shift> getShifts() {
    	// TODO implement - requires getting shifts from course, tutor data
        fetchIfRequired(getInstance().shifts);
    	return getInstance().shifts;
    }

    /**
     * Check if data is cached (i.e. has already been fetched. If not, fetch from the ULC server.
     * @param reference - reference being checked if it has been fetched
     * @param <T> - generic type for reference
     */
    private static <T> void fetchIfRequired(T reference) {
    	if (!isCached(reference)) {
    		fetch();
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

    /**
     * Fetch data from the ULC server and update the data saved in the DataStore instance. This "caches" the
     * data that is fetched.
     */
    private static void fetch() {
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
