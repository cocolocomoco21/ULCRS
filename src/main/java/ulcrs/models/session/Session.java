package ulcrs.models.session;

import ulcrs.GsonFactory;
import ulcrs.models.schedule.Schedule;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Session {

    private static final String WORKSPACE_PATH = "workspace/";

    private String name;
    private Schedule existingSchedule;
    private Date createdTimestamp;
    private Date lastEditedTimestamp;

    public Session() throws IOException {
        lastEditedTimestamp = createdTimestamp = new Date();
        name = "session_" + createdTimestamp.getTime() + ".json";
        save();
    }

    public void save() throws IOException {
        saveAs(name);
    }

    public void saveAs(String filename) throws IOException {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(WORKSPACE_PATH + filename);
            printWriter.println(GsonFactory.getGson().toJson(this));
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    public String getName() {
        return name;
    }

    public Schedule getExistingSchedule() {
        return existingSchedule;
    }

    public void setExistingSchedule(Schedule existingSchedule) {
        this.existingSchedule = existingSchedule;
        lastEditedTimestamp = new Date();
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public Date getLastEditedTimestamp() {
        return lastEditedTimestamp;
    }
}
