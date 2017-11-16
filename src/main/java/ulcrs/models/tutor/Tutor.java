package ulcrs.models.tutor;

import com.google.gson.annotations.Expose;

public class Tutor {

	@Expose
    private int id;
    
	@Expose
	private String firstName;
	
	@Expose
	private String lastName;
    
	@Expose
	private TutorPreferences tutorPreferences;
    
	@Expose
	private TutorStatus tutorStatus;

    public Tutor(int id, String firstName, String lastName, TutorPreferences tutorPreferences, TutorStatus tutorStatus) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tutorPreferences = tutorPreferences;
        this.tutorStatus = tutorStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public TutorPreferences getTutorPreferences() {
        return tutorPreferences;
    }

    public void setTutorPreferences(TutorPreferences tutorPreferences) {
        this.tutorPreferences = tutorPreferences;
    }

    public TutorStatus getTutorStatus() {
        return tutorStatus;
    }

    public void setTutorStatus(TutorStatus tutorStatus) {
        this.tutorStatus = tutorStatus;
    }
}
