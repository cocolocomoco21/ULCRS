package ulcrs.scheduler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.slf4j.LoggerFactory;
import ulcrs.GsonFactory;
import ulcrs.models.course.Course;
import ulcrs.models.schedule.Schedule;
import ulcrs.models.shift.ScheduledShift;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ORToolsScheduler {

    private static final String SCHEDULER_PATH = "../scheduler-src/";
    private static final String SCHEDULER_RESOURCE_PATH = SCHEDULER_PATH + "resources/";
    private static final int BUFFER_SIZE = 4096;

    private static final String TUTOR_FILENAME = "tutor.json";
    private static final String COURSE_FILENAME = "course.json";
    private static final String SHIFT_FILENAME = "shift.json";
    private static final String SCHEDULE_FILENAME = "schedule.json";

    private static final String SCHEDULER_SCRIPT_PATH = SCHEDULER_PATH + "scheduler.py";

    private static org.slf4j.Logger log = LoggerFactory.getLogger(ORToolsScheduler.class);
    private static Gson gson = GsonFactory.getExposeOnlyGson();

    protected static List<Schedule> schedule(List<Tutor> tutors, List<Course> courses, List<Shift> shifts) {

        List<Schedule> emptyList = new ArrayList<>();

        try {
            writeJsonToScheduler(TUTOR_FILENAME, tutors);
            writeJsonToScheduler(COURSE_FILENAME, courses);
            writeJsonToScheduler(SHIFT_FILENAME, shifts);
        } catch (IOException e) {
            log.warn("Cannot save file to scheduler resource folder", e);
            return emptyList;
        }

        try {
            callScheduler();
        } catch (InterruptedException e) {
            log.warn("Scheduler script interrupted", e);
            return emptyList;
        } catch (IOException e) {
            log.warn("Cannot run scheduler script", e);
            return emptyList;
        }

        try {
            return loadSchedulesFromScheduler();
        } catch (IOException e) {
            log.warn("Cannot load generated schedule file", e);
            return emptyList;
        }
    }

    private static void writeJsonToScheduler(String filename, Object object) throws IOException {
        File path = new File(SCHEDULER_RESOURCE_PATH);
        path.mkdirs();

        File file = new File(SCHEDULER_RESOURCE_PATH + filename);
        file.createNewFile();

        FileWriter writer = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(writer, BUFFER_SIZE);
        bufferedWriter.write(gson.toJson(object));
        bufferedWriter.close();
    }

    private static void callScheduler() throws IOException, InterruptedException {
        String command = String.join(" ", new String[]{
                "python",
                SCHEDULER_SCRIPT_PATH,
                SCHEDULER_RESOURCE_PATH + TUTOR_FILENAME,
                SCHEDULER_RESOURCE_PATH + COURSE_FILENAME,
                SCHEDULER_RESOURCE_PATH + SHIFT_FILENAME,
                SCHEDULER_RESOURCE_PATH + SCHEDULE_FILENAME
        });
        log.info("Running command: " + command);
        Process p = Runtime.getRuntime().exec(command);
        p.waitFor();
    }

    private static List<Schedule> loadSchedulesFromScheduler() throws IOException {
        FileReader fileReader = new FileReader(SCHEDULER_RESOURCE_PATH + SCHEDULE_FILENAME);
        JsonReader reader = new JsonReader(fileReader);
        List<Schedule> schedules = gson.fromJson(reader, new TypeToken<List<Schedule>>(){}.getType());
        reader.close();
        fileReader.close();
        return schedules;
    }
}
