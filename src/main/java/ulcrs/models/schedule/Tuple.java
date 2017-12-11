package ulcrs.models.schedule;

import ulcrs.models.course.Course;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;

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
        StringBuilder builder = new StringBuilder();
        // Tutor
        builder.append("Tutor: ")
                .append(tutor.getId())
                .append(", ")
                .append(tutor.getFirstName())
                .append(" ")
                .append(tutor.getLastName())
                .append(" | ");

        // Course
        builder.append("Course: ")
                .append(course.getId())
                .append(", ")
                .append(course.getName())
                .append(" | ");

        // Shift
        builder.append("Shift: ")
                .append(shift.getId())
                .append(", ")
                .append(shift.getDay().name());

        return builder.toString();
    }
}
