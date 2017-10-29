package ulcrs.models.shift;

import java.util.Map;
import java.util.Set;
import ulcrs.models.course.Course;
import ulcrs.models.tutor.Tutor;

public class ScheduledShift {

    Shift shift;
    Map<Tutor, Set<Course>> tutorsToSets;
}
