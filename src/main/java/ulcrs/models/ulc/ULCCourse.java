package ulcrs.models.ulc;

import ulcrs.models.course.Course;
import ulcrs.models.course.CourseRequirements;

public class ULCCourse {

    private int id;
    private String name;

    ULCCourse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    // Although this is not the cleanest, courses inherently depend on course requirements, so pass in the already-parsed CourseRequirements here
    public Course toCourse(CourseRequirements courseRequirements) {
        return new Course(this.id, this.name, courseRequirements);
    }
}
