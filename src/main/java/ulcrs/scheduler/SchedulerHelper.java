package ulcrs.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulcrs.data.DataStore;
import ulcrs.models.course.Course;
import ulcrs.models.schedule.Schedule;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;

import java.time.LocalDateTime;
import java.util.List;

public class SchedulerHelper {
    private static Logger log = LoggerFactory.getLogger(SchedulerHelper.class);

    private static List<Schedule> generatedSchedules;
    private static boolean isScheduling = false;
    private static LocalDateTime schedulingStart;

    public static boolean generateSchedule(int timeLimitInSecond, int solutionLimit) {
        // TODO implement
        // This is where the algorithm's entry point will be. This will call the scheduling algorithm with the
        // specified tutors, courses, and shifts from DataStore, and return the list of schedules generated from
        // those constraints

        // TODO handle getting data when it has not been fetched (i.e. don't use empty string for cookie in order to get this data)
        List<Tutor> tutors = DataStore.getTutors("");
        List<Course> courses = DataStore.getCourses("");
        List<Shift> shifts = DataStore.getShifts("");

        // Start new thread to handle scheduling so thread handling the HTTP request can return
        Thread scheduleThread = new Thread(() -> {
            schedulingStart = LocalDateTime.now();
            isScheduling = true;

            // Run scheduling algorithm
            generatedSchedules = ORToolsScheduler.schedule(tutors, courses, shifts, timeLimitInSecond, solutionLimit);
            isScheduling = false;
        });
        scheduleThread.start();

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
