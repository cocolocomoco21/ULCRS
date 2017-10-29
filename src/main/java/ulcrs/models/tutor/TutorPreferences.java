package ulcrs.models.tutor;

import lombok.Data;
import ulcrs.models.course.Course;
import ulcrs.models.shift.Shift;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class TutorPreferences {

    Map<TutorPreferenceRank, Set<Course>> coursePreferences;
    Map<TutorPreferenceRank, Set<Shift>> shiftPreferences;
    Map<TutorPreferenceRank, Integer> shiftFrequencyPreferences;

    public TutorPreferences() {
        coursePreferences = new HashMap<>();
        shiftPreferences = new HashMap<>();
        shiftFrequencyPreferences = new HashMap<>();
    }
}
