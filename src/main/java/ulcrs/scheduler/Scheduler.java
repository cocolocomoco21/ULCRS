package ulcrs.scheduler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import ulcrs.data.DataStore;
import ulcrs.models.schedule.Schedule;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;

public class Scheduler {
    private static List<Schedule> generatedSchedules;
    private static boolean isScheduling = false;
    private static LocalDateTime schedulingStart;

    public static boolean generateSchedule() {
        // TODO implement
        // This is where the algorithm's entry point will be. This will call the scheduling algorithm with the
        // specified tutors, courses, and shifts from DataStore, and return the list of schedules generated from
        // those constraints

        // TODO we want to spin off a thread here to do our scheduling likely.
        // The scheduling should not be done on the same thread as the thread handling the HTTP request, as we need
        // to return data for the request to avoid a timeout

        isScheduling = true;
        schedulingStart = LocalDateTime.now();

        // Start scheduling algorithm


        // For now, return mock data
        InputStream is = DataStore.class.getClassLoader().getResourceAsStream("mockSchedule_FrontendExpects.json");
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        generatedSchedules = new Gson().fromJson(reader, new TypeToken<List<Schedule>>() {}.getType());

        isScheduling = false;
        return true;
    }

    public static List<Schedule> fetchGeneratedSchedules() {
        if (!isComplete()) {
            return null;
        }

        return generatedSchedules;
    }

    private static boolean isComplete() {
        return schedulingStart != null && !isScheduling;
    }

    public static boolean verifySchedule(Schedule schedule) {
        // TODO implement
        return false;
    }

}
