package ulcrs.models.tutor;

import ulcrs.models.course.Course;
import ulcrs.models.rank.Rank;
import ulcrs.models.shift.Shift;

import java.util.Map;
import java.util.Set;

public class TutorPreferences {

    private Map<Rank, Set<Course>> coursePreferences;
    private Map<Rank, Set<Shift>> shiftPreferences;
    private Map<Rank, Integer> shiftFrequencyPreferences;

    public TutorPreferences(Map<Rank, Set<Course>> coursePreferences, Map<Rank, Set<Shift>> shiftPreferences, Map<Rank, Integer> shiftFrequencyPreferences) {
        this.coursePreferences = coursePreferences;
        this.shiftPreferences = shiftPreferences;
        this.shiftFrequencyPreferences = shiftFrequencyPreferences;
    }

    public Map<Rank, Set<Course>> getCoursePreferences() {
        return coursePreferences;
    }

    public void setCoursePreferences(Map<Rank, Set<Course>> coursePreferences) {
        this.coursePreferences = coursePreferences;
    }

    public Map<Rank, Set<Shift>> getShiftPreferences() {
        return shiftPreferences;
    }

    public void setShiftPreferences(Map<Rank, Set<Shift>> shiftPreferences) {
        this.shiftPreferences = shiftPreferences;
    }

    public Map<Rank, Integer> getShiftFrequencyPreferences() {
        return shiftFrequencyPreferences;
    }

    public void setShiftFrequencyPreferences(Map<Rank, Integer> shiftFrequencyPreferences) {
        this.shiftFrequencyPreferences = shiftFrequencyPreferences;
    }
}
