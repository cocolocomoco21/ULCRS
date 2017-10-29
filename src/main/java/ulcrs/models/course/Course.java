package ulcrs.models.course;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Course {
    private int id;
    private String name;
    private CourseRequirements courseRequirements;
}
