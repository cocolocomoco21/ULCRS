package ulcrs.data;

import ulcrs.models.course.Course;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;

import java.util.ArrayList;
import java.util.List;

public class ParsedULCResponse {
    List<Course> courses;
    List<Shift> shifts;
    List<Tutor> tutors;

    ParsedULCResponse() {
        this.courses = new ArrayList<>();
        this.shifts = new ArrayList<>();
        this.tutors = new ArrayList<>();
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }

    public List<Tutor> getTutors() {
        return tutors;
    }

    public void setTutors(List<Tutor> tutors) {
        this.tutors = tutors;
    }
}
