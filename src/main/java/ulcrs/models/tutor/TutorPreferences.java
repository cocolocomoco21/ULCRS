package ulcrs.models.tutor;

import java.util.Map;
import java.util.Set;
import ulcrs.models.course.Course;
import ulcrs.models.rank.Rank;
import ulcrs.models.shift.Shift;

public class TutorPreferences {

    Map<Rank, Set<Course>> coursePreferences;
    Map<Rank, Set<Shift>> shiftPreferences;
    Map<Rank, Integer> shiftFrequencyPreferences;

    public TutorPreferences(Map<Rank, Set<Course>> coursePreferences, Map<Rank, Set<Shift>> shiftPreferences, Map<Rank, Integer> shiftFrequencyPreferences) {
        this.coursePreferences = coursePreferences;
        this.shiftPreferences = shiftPreferences;
        this.shiftFrequencyPreferences = shiftFrequencyPreferences;
    }
}
