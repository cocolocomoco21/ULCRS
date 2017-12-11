package ulcrs.models.schedule;

import ulcrs.models.course.Course;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;

import java.util.Objects;

/**
 * Immutable object representing a tuple of tutor-course-shift, used for scheduling.
 */
public class Tuple {

    private Tutor tutor;
    private Course course;
    private Shift shift;

    public Tuple(Tutor tutor, Course course, Shift shift) {
        this.tutor = tutor;
        this.course = course;
        this.shift = shift;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public Course getCourse() {
        return course;
    }

    public Shift getShift() {
        return shift;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Tuple{");
        builder.append("tutor=")
                .append(tutor.getId())
                .append(", ")
                .append(tutor.getFirstName())
                .append(" ")
                .append(tutor.getLastName())
                .append(" | ");
        builder.append("course=")
                .append(course.getId())
                .append(", ")
                .append(course.getName())
                .append(" | ");
        builder.append("shift=")
                .append(shift.getId())
                .append(", ")
                .append(shift.getDay().name());
        builder.append("}");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return Objects.equals(tutor, tuple.tutor) &&
                Objects.equals(course, tuple.course) &&
                Objects.equals(shift, tuple.shift);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tutor, course, shift);
    }
}
