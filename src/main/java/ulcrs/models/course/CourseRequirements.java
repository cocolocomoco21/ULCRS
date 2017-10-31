package ulcrs.models.course;

import ulcrs.models.shift.Shift;

import java.util.Set;

public class CourseRequirements {

    private Set<Shift> requiredShifts;
    private int requiredShiftAmount;
    private int preferredShiftAmount;
    private CourseIntensity intensity;

    public CourseRequirements(Set<Shift> requiredShifts, int requiredShiftAmount, int preferredShiftAmount, CourseIntensity intensity) {
        this.requiredShifts = requiredShifts;
        this.requiredShiftAmount = requiredShiftAmount;
        this.preferredShiftAmount = preferredShiftAmount;
        this.intensity = intensity;
    }

    public Set<Shift> getRequiredShifts() {
        return requiredShifts;
    }

    public void setRequiredShifts(Set<Shift> requiredShifts) {
        this.requiredShifts = requiredShifts;
    }

    public int getRequiredShiftAmount() {
        return requiredShiftAmount;
    }

    public void setRequiredShiftAmount(int requiredShiftAmount) {
        this.requiredShiftAmount = requiredShiftAmount;
    }

    public int getPreferredShiftAmount() {
        return preferredShiftAmount;
    }

    public void setPreferredShiftAmount(int preferredShiftAmount) {
        this.preferredShiftAmount = preferredShiftAmount;
    }

    public CourseIntensity getIntensity() {
        return intensity;
    }

    public void setIntensity(CourseIntensity intensity) {
        this.intensity = intensity;
    }
}
