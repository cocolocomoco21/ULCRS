package ulcrs.models.course;

import lombok.Data;
import ulcrs.models.shift.Shift;

import java.util.HashSet;
import java.util.Set;

@Data
public class CourseRequirements {

    Set<Shift> requiredShifts;
    int requiredShiftAmount;
    int preferredShiftAmount;
    CourseIntensity intensity;

    public CourseRequirements() {
        requiredShifts = new HashSet<>();
    }
}
