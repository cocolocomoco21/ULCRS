package ulcrs.models.shift;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

import com.google.gson.annotations.Expose;

public class Shift {

    @Expose
    private int id;

    @Expose
    private DayOfWeek day;
    
    @Expose(serialize = false)
    private LocalTime startTime;
    
    @Expose(serialize = false)
    private LocalTime endTime;

    public Shift(int id, DayOfWeek day, LocalTime startTime, LocalTime endTime) {
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Shift{");
        sb.append("id=").append(id);
        sb.append(", day=").append(day);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append('}');
        return sb.toString();
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
