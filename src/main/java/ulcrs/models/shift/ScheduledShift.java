package ulcrs.models.shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import ulcrs.models.course.Course;
import ulcrs.models.tutor.Tutor;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
public class ScheduledShift {

    Shift shift;
    Map<Tutor, Set<Course>> tutorsToSets;
}
