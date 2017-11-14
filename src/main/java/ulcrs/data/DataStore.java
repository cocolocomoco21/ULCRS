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


    public static Tutor getTutor(int id) {
        // TODO have this reference DataStore.tutors
        InputStream is = DataStore.class.getClassLoader().getResourceAsStream("mockTutorsFull.json");
        JsonReader reader = new JsonReader(new InputStreamReader(is));

        List<Tutor> tutors = new Gson().fromJson(reader, new TypeToken<List<Tutor>>() {}.getType());

        return tutors.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static Course getCourse(int id) {
        return getInstance().courses.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static Shift getShift(int id) {
        return getInstance().shifts.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static List<Tutor> getTutors() {
        if (checkNotNull(getInstance().tutors)) {
            // Fetch from ULC - TODO

            // (For now, just set tutors to be this mock data)
            InputStream is = DataStore.class.getClassLoader().getResourceAsStream("mockTutors.json");
            JsonReader reader = new JsonReader(new InputStreamReader(is));

            getInstance().tutors = new Gson().fromJson(reader, new TypeToken<List<Tutor>>() {}.getType());
        }

        return getInstance().tutors;
    }

    private static <T> boolean checkNotNull(T reference) {
        return reference == null;
    }
}
