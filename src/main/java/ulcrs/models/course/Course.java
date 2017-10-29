package ulcrs.models.course;

public class Course {
    int id;
    String name;
    CourseRequirements courseRequirements;

    public Course(int id, String name, CourseRequirements courseRequirements) {
        this.id = id;
        this.name = name;
        this.courseRequirements = courseRequirements;
    }
}
