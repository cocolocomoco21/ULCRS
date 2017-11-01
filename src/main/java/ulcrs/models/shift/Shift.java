package ulcrs.models.shift;

import java.time.DayOfWeek;
import java.time.OffsetTime;
import java.util.Objects;

public class Shift {

    private int id;
    private DayOfWeek day;
    private OffsetTime startTime;
    private OffsetTime endTime;

    public Shift(int id, DayOfWeek day, OffsetTime startTime, OffsetTime endTime) {
        this.id = id;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public OffsetTime getStartTime() {
        return startTime;
    }

    public void setStartTime(OffsetTime startTime) {
        this.startTime = startTime;
    }

    public OffsetTime getEndTime() {
        return endTime;
    }

    public void setEndTime(OffsetTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shift shift = (Shift) o;
        return id == shift.id &&
                day == shift.day &&
                Objects.equals(startTime, shift.startTime) &&
                Objects.equals(endTime, shift.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, day, startTime, endTime);
    }
}
