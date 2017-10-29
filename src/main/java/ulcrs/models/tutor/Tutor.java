package ulcrs.models.tutor;

public class Tutor {
    int id;
    String firstName;
    String lastName;
    TutorPreferences tutorPreferences;
    TutorStatus tutorStatus;

    public Tutor(int id, String firstName, String lastName, TutorPreferences tutorPreferences, TutorStatus tutorStatus) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tutorPreferences = tutorPreferences;
        this.tutorStatus = tutorStatus;
    }
}
