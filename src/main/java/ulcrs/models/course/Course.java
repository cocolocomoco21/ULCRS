package ulcrs.models.course;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Course {

    int id;
    String name;
    CourseRequirements courseRequirements;
}
