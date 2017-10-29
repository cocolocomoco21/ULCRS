package ulcrs.models.shift;

import java.time.DayOfWeek;
import java.time.OffsetTime;

public class Shift {
    int id;
    DayOfWeek day;
    OffsetTime startTime;
    OffsetTime endTime;

    public Shift(int id, DayOfWeek day, OffsetTime startTime, OffsetTime endTime) {
        this.id = id;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
