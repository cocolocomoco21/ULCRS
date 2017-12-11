package ulcrs.models.ulc;

import com.google.gson.annotations.SerializedName;
import ulcrs.models.course.CourseIntensity;
import ulcrs.models.course.CourseRequirements;
import ulcrs.models.shift.Shift;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ULCCourseRequirements {

    @SerializedName("courseID")
    private int courseId;

    private String intensity;

    // Minimum required number of times per week
    private int timesPerWeek;

    // Minimum number of tutors required per week
    private int numTutors;

    private List<ULCSpecific> specifics;


    ULCCourseRequirements(int courseId, String intensity, int timesPerWeek, int numTutors, List<ULCSpecific> specifics) {
        this.courseId = courseId;
        this.intensity = intensity;
        this.timesPerWeek = timesPerWeek;
        this.numTutors = numTutors;
        this.specifics = specifics;
    }

    public int getCourseId() {
        return this.courseId;
    }

    // Although this is not the cleanest, courses inherently depend on shifts, so pass in the already-parsed Shifts here
    public CourseRequirements toCourseRequirements(HashMap<Integer, Shift> shifts) {
        Map<Integer, Integer> numTutorsPerShift = new HashMap<>();
        Set<Shift> requiredShifts = new TreeSet<>(Comparator.comparingInt(Shift::getId));

        this.specifics.forEach(specific -> {
            int shiftId = specific.getShiftId();
            int numTutors = specific.getNumTutors();

            Shift shift = shifts.get(shiftId);
            if (shift != null) {
                requiredShifts.add(shift);
                numTutorsPerShift.put(shift.getId(), numTutors);
            }
        });

        CourseIntensity intensity = CourseIntensity.fromString(this.intensity);

        return new CourseRequirements(requiredShifts, this.timesPerWeek, 0, intensity, numTutorsPerShift, this.numTutors);
    }
}
