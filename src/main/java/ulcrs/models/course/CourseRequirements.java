package ulcrs.models.course;

import java.util.Set;
import ulcrs.models.shift.Shift;

public class CourseRequirements {
    Set<Shift> requiredShifts;
    int requiredShiftAmount;
    int preferredShiftAmount;
    CourseIntensity intensity;

    public CourseRequirements(Set<Shift> requiredShifts, int requiredShiftAmount, int preferredShiftAmount, CourseIntensity intensity) {
        this.requiredShifts = requiredShifts;
        this.requiredShiftAmount = requiredShiftAmount;
        this.preferredShiftAmount = preferredShiftAmount;
        this.intensity = intensity;
    }
}
