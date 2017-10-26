package ulcrs.models.course;

public class Course {
    private int id;
    private String name;
    private CourseRequirements courseRequirements;

    public Course(int id, String name, CourseRequirements courseRequirements) {
        this.id = id;
        this.name = name;
        this.courseRequirements = courseRequirements;
    }
}
