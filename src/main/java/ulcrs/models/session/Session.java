package ulcrs.models.session;

import ulcrs.models.schedule.Schedule;

public class Session {

    String name;
    Schedule existingSchedule;
    long generatedTimestamp;
    long lastEditedTimestamp;

    public void save() {
        // TODO: implement
    }

    public boolean saveAs(String filename) {
        // TODO: implement
        return false;
    }
}
