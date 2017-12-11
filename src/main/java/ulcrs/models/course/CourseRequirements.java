package ulcrs.models.course;

import com.google.gson.annotations.Expose;
import ulcrs.models.shift.Shift;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CourseRequirements {

    @Expose
    private Set<Shift> requiredShifts;

    @Expose
    private int requiredShiftAmount;

    @Expose
    private int preferredShiftAmount;   // Note: this is not currently used, but is kept here for simplicity

    @Expose
    private CourseIntensity intensity;

    @Expose
    private Map<Integer, Integer> numTutorsPerShift;

    @Expose
    private int numTutorsPerWeek;


    public CourseRequirements(Set<Shift> requiredShifts, int requiredShiftAmount, int preferredShiftAmount, CourseIntensity intensity,
                              Map<Integer, Integer> numTutorsPerShift, int numTutorsPerWeek) {
        this.requiredShifts = requiredShifts;
        this.requiredShiftAmount = requiredShiftAmount;
        this.preferredShiftAmount = preferredShiftAmount;
        this.intensity = intensity;
        this.numTutorsPerShift = numTutorsPerShift;
        this.numTutorsPerWeek = numTutorsPerWeek;
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

    public Map<Integer, Integer> getNumTutorsPerShift() {
        return numTutorsPerShift;
    }

    public int getNumTutorsPerWeek() {
        return numTutorsPerWeek;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseRequirements that = (CourseRequirements) o;
        return requiredShiftAmount == that.requiredShiftAmount &&
                preferredShiftAmount == that.preferredShiftAmount &&
                numTutorsPerWeek == that.numTutorsPerWeek &&
                Objects.equals(requiredShifts, that.requiredShifts) &&
                intensity == that.intensity &&
                Objects.equals(numTutorsPerShift, that.numTutorsPerShift);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requiredShifts, requiredShiftAmount, preferredShiftAmount, intensity, numTutorsPerShift, numTutorsPerWeek);
    }
}
