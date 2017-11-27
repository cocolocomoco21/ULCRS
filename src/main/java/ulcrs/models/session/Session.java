package ulcrs.models.session;

import ulcrs.models.schedule.Schedule;

import java.util.Date;

public class Session {

    private String name;
    private Schedule existingSchedule;
    private Date createdTimestamp;
    private Date lastEditedTimestamp;

    public Session() {
        lastEditedTimestamp = createdTimestamp = new Date();
        name = "session_" + createdTimestamp.getTime() + ".json";
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
