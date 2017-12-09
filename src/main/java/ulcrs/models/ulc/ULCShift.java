package ulcrs.models.ulc;

import ulcrs.models.shift.Shift;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class ULCShift {

    private int id;
    private int day;
    private String startTime;
    private String endTime;

    public ULCShift(int id, int day, String startTime, String endTime) {
        this.id = id;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Shift toShift() {
        int dayOfWeekTranslation = this.day + 1;  // Data from ULC is zero-based, but Java's DayOfWeek is not
        DayOfWeek day = DayOfWeek.of(dayOfWeekTranslation);

        LocalDateTime startTime = LocalDateTime.parse(this.startTime);
        LocalDateTime endTime = LocalDateTime.parse(this.endTime);

        return new Shift(id, day, startTime.toLocalTime(), endTime.toLocalTime());
    }
}
