package ulcrs.scheduler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulcrs.data.DataStore;
import ulcrs.models.course.Course;
import ulcrs.models.schedule.Schedule;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;

public class Scheduler {
    private static Logger log = LoggerFactory.getLogger(Scheduler.class);

    private static List<Schedule> generatedSchedules;
    private static boolean isScheduling = false;
    private static LocalDateTime schedulingStart;

    public static boolean generateSchedule() {
        // TODO implement
        // This is where the algorithm's entry point will be. This will call the scheduling algorithm with the
        // specified tutors, courses, and shifts from DataStore, and return the list of schedules generated from
        // those constraints

        List<Tutor> tutors = DataStore.getTutors();
        List<Course> courses = DataStore.getCourses();
        List<Shift> shifts = DataStore.getShifts();

        // Start new thread to handle scheduling so thread handling the HTTP request can return
        Thread scheduleThread = new Thread(() -> {
            schedule(tutors, courses, shifts);
        });
        scheduleThread.start();

        return true;
    }


    private static boolean schedule(List<Tutor> tutors, List<Course> courses, List<Shift> shifts) {
        isScheduling = true;
        schedulingStart = LocalDateTime.now();

        // Start scheduling algorithm

        // Run scheduling algorithm
        // For now, return mock data
        InputStream is = DataStore.class.getClassLoader().getResourceAsStream("mockSchedule_FrontendExpects.json");
        JsonReader reader = new JsonReader(new InputStreamReader(is));

        // Finish scheduling algorithm
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
