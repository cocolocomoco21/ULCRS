package ulcrs.models.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import ulcrs.models.schedule.Schedule;

@AllArgsConstructor
@Data
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
