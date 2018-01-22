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
import java.util.stream.Collectors;

public class SchedulerHelper {
    private static Logger log = LoggerFactory.getLogger(SchedulerHelper.class);

    private static List<Schedule> generatedSchedules;
    private static boolean isScheduling = false;
    private static LocalDateTime schedulingStart;

    /**
     * This is the algorithm's entry point. This starts the scheduling algorithm with the specified tutors, courses,
     * and shifts from DataStore. This method does not return the list of generated schedules, but rather updates the
     * static list of schedules once the scheduling algorithm has completed. This method does returns the status of the
     * scheduling, instead of the list of generated schedules, so the HTTP request can return.
     *
     * @return boolean - status of generation of schedules.
     */
    public static boolean generateSchedule(int timeLimitInSecond, int solutionLimit, List<Integer> excludedIds) {
        // TODO handle getting data when it has not been fetched (i.e. don't use empty string for cookie in order to get this data)
        List<Tutor> tutors = DataStore.getTutors("").stream()
                .filter(i -> !excludedIds.contains(i.getId()))
                .collect(Collectors.toList());
        List<Course> courses = DataStore.getCourses("");
        List<Shift> shifts = DataStore.getShifts("");

        // Start new thread to handle scheduling so thread handling the HTTP request can return
        Thread scheduleThread = new Thread(() -> {
            schedulingStart = LocalDateTime.now();
            isScheduling = true;

            log.info("Starting scheduling at " + LocalDateTime.now().toLocalTime().toString() + "...");

            // Run scheduling algorithm
            generatedSchedules = ORToolsScheduler.schedule(tutors, courses, shifts, timeLimitInSecond, solutionLimit);
            isScheduling = false;

            log.info("Ending scheduling at " + LocalDateTime.now().toLocalTime().toString() + "...");
        });
        scheduleThread.start();

        return true;
    }

    /**
     * Access generated schedules, if they exist. If not, the scheduling algorithm has not been started (see #generateSchedule())
     * or the algorithm has not completed.
     *
     * @return List<Schedule> - the list of generated schedules according to the scheduling algorithm.
     */
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
