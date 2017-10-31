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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CourseRequirements getCourseRequirements() {
        return courseRequirements;
    }

    public void setCourseRequirements(CourseRequirements courseRequirements) {
        this.courseRequirements = courseRequirements;
    }
}
