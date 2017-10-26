package models;

public class Tutor {
    private int id;
    private String firstName;
    private String lastName;
    private TutorPreferences tutorPreferences;
    private TutorStatus tutorStatus;


    public Tutor(int id, String firstName, String lastName, TutorPreferences tutorPreferences, TutorStatus tutorStatus) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tutorPreferences = tutorPreferences;
        this.tutorStatus = tutorStatus;
    }

}
