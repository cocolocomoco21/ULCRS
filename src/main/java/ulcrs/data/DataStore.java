package ulcrs.data;

import ulcrs.models.course.Course;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataStore {

    private List<Tutor> tutors = new ArrayList();;
    private List<Course> courses = new ArrayList();;
    private List<Shift> shifts = new ArrayList();;
    private LocalDateTime timeFetched = LocalDateTime.MIN;

    private static DataStore dataStore;

    public static DataStore getInstance() {
        if (dataStore == null) {
            dataStore = new DataStore();
        }

        return dataStore;
    }


    public Tutor findTutor(int id) {
        return tutors.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Course findCourse(int id) {
        return courses.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Shift findShift(int id) {
        return shifts.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

}
