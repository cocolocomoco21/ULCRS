package ulcrs.models.session;

import ulcrs.models.schedule.Schedule;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Session {

    private String name;
    private Schedule existingSchedule;
    private LocalDateTime createdTimestamp;
    private LocalDateTime lastEditedTimestamp;

    public Session() {
        this("session_" + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + ".json");
    }

    public Session(String name) {
        this.name = name;
        createdTimestamp = LocalDateTime.now();
        lastEditedTimestamp = createdTimestamp;
    }

    public String getName() {
        return name;
    }

    public Schedule getExistingSchedule() {
        return existingSchedule;
    }

    public void setExistingSchedule(Schedule existingSchedule) {
        this.existingSchedule = existingSchedule;
        lastEditedTimestamp = LocalDateTime.now();
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public LocalDateTime getLastEditedTimestamp() {
        return lastEditedTimestamp;
    }
}
