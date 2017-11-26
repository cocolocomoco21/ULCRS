package ulcrs.models.shift;

import com.google.gson.annotations.Expose;
import ulcrs.models.course.Course;
import ulcrs.models.tutor.Tutor;

import java.util.HashSet;
import java.util.Set;

public class Assignment {

    @Expose
    private Tutor tutor;

    @Expose
    private Set<Course> courses;

    public Assignment(Tutor tutor) {
        this.tutor = tutor;
        this.courses = new HashSet<Course>();
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }
}
