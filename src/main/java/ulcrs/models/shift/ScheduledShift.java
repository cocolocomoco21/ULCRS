package ulcrs.models.shift;

import ulcrs.models.course.Course;
import ulcrs.models.tutor.Tutor;

import java.util.Map;
import java.util.Set;

public class ScheduledShift {

    private Shift shift;
    private Map<Tutor, Set<Course>> tutorsToSets;

    public ScheduledShift(Shift shift) {
        this.shift = shift;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Map<Tutor, Set<Course>> getTutorsToSets() {
        return tutorsToSets;
    }

    public void setTutorsToSets(Map<Tutor, Set<Course>> tutorsToSets) {
        this.tutorsToSets = tutorsToSets;
    }
}
