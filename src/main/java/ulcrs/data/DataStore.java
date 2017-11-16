package ulcrs.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import ulcrs.models.course.Course;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;

public class DataStore {

    private List<Tutor> tutors;
    private List<Course> courses;
    private List<Shift> shifts;
    private LocalDateTime timeFetched = LocalDateTime.MIN;

    private static DataStore dataStore;

    public static DataStore getInstance() {
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
    
    public static List<Shift> getShifts() {
    	// TODO implement - requires getting shifts from course, tutor data
    	fetchIfRequired(getInstance().courses);
    	return getInstance().shifts;
    }

    private static <T> void fetchIfRequired(T reference) {
    	if (!isCached(reference)) {
    		fetch();
    	}
    }
    
    private static <T> boolean isCached(T reference) {
        return reference != null;
    }
    
    private static void fetch() {
    	// Fetch from ULC - TODO
    	// For now, just get data from mock data    	
    	
    	// Tutor
        InputStream is = DataStore.class.getClassLoader().getResourceAsStream("mockTutorsFull.json");
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        getInstance().tutors = new Gson().fromJson(reader, new TypeToken<List<Tutor>>() {}.getType());
        
    	// Course
        is = DataStore.class.getClassLoader().getResourceAsStream("mockCoursesFull.json");
        reader = new JsonReader(new InputStreamReader(is));
        getInstance().courses = new Gson().fromJson(reader, new TypeToken<List<Course>>() {}.getType());
        
        // TODO using tutors and courses, get all shift information and save into getInstance().shifts
        
        getInstance().timeFetched = LocalDateTime.now();
    }  
    
}
