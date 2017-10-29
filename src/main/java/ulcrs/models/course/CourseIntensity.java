package ulcrs.models.course;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CourseIntensity {

    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    @Getter
    private String value;
}
