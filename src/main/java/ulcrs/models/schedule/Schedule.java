package ulcrs.models.schedule;

import lombok.Data;
import lombok.NoArgsConstructor;
import ulcrs.models.shift.ScheduledShift;

import java.util.Set;

@NoArgsConstructor
@Data
public class Schedule {

    Set<ScheduledShift> scheduledShifts;
    double rating;

    private void rate() {
        // TODO: implement
    }

    public boolean verify() {
        // TODO: implement
        return false;
    }
}
