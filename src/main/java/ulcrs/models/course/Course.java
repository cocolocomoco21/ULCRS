package ulcrs.models.course;

import com.google.gson.annotations.Expose;

import java.util.Objects;

public class Course {

    @Expose
    private int id;
    
    @Expose
    private String name;
    
    @Expose
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Course course = (Course) o;
        return id == course.id &&
                Objects.equals(name, course.name) &&
                Objects.equals(courseRequirements, course.courseRequirements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, courseRequirements);
    }
}
