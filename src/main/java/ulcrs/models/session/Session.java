package ulcrs.models.session;

import ulcrs.models.schedule.Schedule;

import java.time.LocalTime;

public class Session {

    private String name;
    private Schedule existingSchedule;
    private LocalTime generatedTimestamp;
    private LocalTime lastEditedTimestamp;

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

    public LocalTime getGeneratedTimestamp() {
        return generatedTimestamp;
    }

    public void setGeneratedTimestamp(LocalTime generatedTimestamp) {
        this.generatedTimestamp = generatedTimestamp;
    }

    public LocalTime getLastEditedTimestamp() {
        return lastEditedTimestamp;
    }

    public void setLastEditedTimestamp(LocalTime lastEditedTimestamp) {
        this.lastEditedTimestamp = lastEditedTimestamp;
    }
}
