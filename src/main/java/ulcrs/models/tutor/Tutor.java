package ulcrs.models.tutor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
public class Tutor {
    private int id;
    private String firstName;
    private String lastName;
    private TutorPreferences tutorPreferences;
    private TutorStatus tutorStatus;
}
