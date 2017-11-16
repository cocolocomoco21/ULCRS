package ulcrs.models.course;

import ulcrs.models.shift.Shift;

import java.util.Objects;
import java.util.Set;

import com.google.gson.annotations.Expose;

public class CourseRequirements {

	@Expose
    private Set<Shift> requiredShifts;
    
	@Expose
	private int requiredShiftAmount;
    
	@Expose
	private int preferredShiftAmount;
    
	@Expose
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CourseRequirements that = (CourseRequirements) o;
        return requiredShiftAmount == that.requiredShiftAmount &&
                preferredShiftAmount == that.preferredShiftAmount &&
                Objects.equals(requiredShifts, that.requiredShifts) &&
                intensity == that.intensity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(requiredShifts, requiredShiftAmount, preferredShiftAmount, intensity);
    }
}
