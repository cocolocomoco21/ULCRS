package ulcrs.models.session;

import ulcrs.models.schedule.Schedule;

import java.time.LocalDateTime;

public class Session {

    private String name;
    private Schedule existingSchedule;
    private LocalDateTime generatedTimestamp;
    private LocalDateTime lastEditedTimestamp;

    public void save() {
        // TODO: implement
    }

    public boolean saveAs(String filename) {
        // TODO: implement
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Schedule getExistingSchedule() {
        return existingSchedule;
    }

    public void setExistingSchedule(Schedule existingSchedule) {
        this.existingSchedule = existingSchedule;
    }

    public LocalDateTime getGeneratedTimestamp() {
        return generatedTimestamp;
    }

    public void setGeneratedTimestamp(LocalDateTime generatedTimestamp) {
        this.generatedTimestamp = generatedTimestamp;
    }

    public LocalDateTime getLastEditedTimestamp() {
        return lastEditedTimestamp;
    }

    public void setLastEditedTimestamp(LocalDateTime lastEditedTimestamp) {
        this.lastEditedTimestamp = lastEditedTimestamp;
    }
}
