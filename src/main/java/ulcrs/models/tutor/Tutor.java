package ulcrs.models.tutor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tutor {

    int id;
    String firstName;
    String lastName;
    TutorPreferences tutorPreferences;
    TutorStatus tutorStatus;
}
