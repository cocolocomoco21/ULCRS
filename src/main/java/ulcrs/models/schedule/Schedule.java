package ulcrs.models.schedule;

import ulcrs.models.shift.ScheduledShift;

import java.util.Set;

public class Schedule {

    private Set<ScheduledShift> scheduledShifts;
    private double rating;

    public Schedule(Set<ScheduledShift> scheduledShifts) {
        this.scheduledShifts = scheduledShifts;
        this.rating = 0.0;  // TODO
    }

    private void rate() {
        // TODO: implement
    }

    public boolean verify() {
        // TODO: implement
        return false;
    }

    public Set<ScheduledShift> getScheduledShifts() {
        return scheduledShifts;
    }

    public void setScheduledShifts(Set<ScheduledShift> scheduledShifts) {
        this.scheduledShifts = scheduledShifts;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
