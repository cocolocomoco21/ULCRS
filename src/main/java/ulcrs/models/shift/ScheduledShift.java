package ulcrs.models.shift;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class ScheduledShift {

    @Expose
    private Shift shift;

    @Expose
    private Set<Assignment> assignments;

    public ScheduledShift(Shift shift) {
        this.shift = shift;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Set<Assignment> getAssignments() {
        return this.assignments;
    }

    public void setAssignments(Set<Assignment> assignments) {
        this.assignments = assignments;
    }

    public void addAssignment(Assignment assignment) {
        this.assignments.add(assignment);
    }
}
