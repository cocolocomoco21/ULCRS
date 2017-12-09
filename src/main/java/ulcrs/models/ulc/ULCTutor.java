package ulcrs.models.ulc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import ulcrs.models.course.Course;
import ulcrs.models.rank.Rank;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;
import ulcrs.models.tutor.TutorPreferences;
import ulcrs.models.tutor.TutorStatus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ULCTutor {

    private int id;
    private String firstName;
    private String lastName;
    private String role;
    private List<Integer> prefCourses;
    private List<Integer> willingCourses;

    @SerializedName("prefShift")
    private List<Integer> prefShifts;

    @SerializedName("willingShift")
    private List<Integer> willingShifts;

    private Integer prefShiftAmount;
    private Integer willingShiftAmount;

    public ULCTutor(int id, String firstName, String lastName, String role,
                    List<Integer> prefCourses, List<Integer> willingCourses,
                    List<Integer> prefShfits, List<Integer> willingShifts,
                    Integer prefShiftAmount, Integer willingShiftAmount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.prefCourses = prefCourses;
        this.willingCourses = willingCourses;
        this.prefShifts = prefShfits;
        this.willingShifts = willingShifts;
        this.prefShiftAmount = prefShiftAmount;
        this.willingShiftAmount = willingShiftAmount;
    }

    public Tutor toTutor(HashMap<Integer, Shift> shifts, HashMap<Integer, Course> courses) {
        // Shift preference
        HashMap<Rank, Set<Shift>> shiftPreferences = new HashMap<>();

        HashSet<Shift> preferredShifts = new HashSet<>();
        this.prefShifts.forEach(shiftId -> {
            Shift shift = shifts.get(shiftId);

            // Skip over non-extistent shifts
            if (shift != null) {
                preferredShifts.add(shift);
            }
        });

        HashSet<Shift> willingShifts = new HashSet<>();
        this.willingShifts.forEach(shiftId -> {
            Shift shift = shifts.get(shiftId);

            // Skip over non-existent shifts
            if (shift != null) {
                willingShifts.add(shift);
            }
        });

        shiftPreferences.put(Rank.PREFER, preferredShifts);
        shiftPreferences.put(Rank.WILLING, willingShifts);

        // Course preference
        HashMap<Rank, Set<Course>> coursePreferences = new HashMap<>();

        HashSet<Course> preferredCourses = new HashSet<>();
        this.prefCourses.forEach(courseId -> {
            Course course = courses.get(courseId);

            // Skip over non-extistent courses
            if (course != null) {
                preferredCourses.add(course);
            }
        });

        HashSet<Course> willingCourses = new HashSet<>();
        this.willingCourses.forEach(courseId -> {
            Course course = courses.get(courseId);

            // Skip over non-extistent courses
            if (course != null) {
                willingCourses.add(course);
            }
        });

        coursePreferences.put(Rank.PREFER, preferredCourses);
        coursePreferences.put(Rank.WILLING, willingCourses);

        // Shift frequency preference
        HashMap<Rank, Integer> shiftFrequencyPreference = new HashMap<>();
        shiftFrequencyPreference.put(Rank.PREFER, this.prefShiftAmount);
        shiftFrequencyPreference.put(Rank.WILLING, this.willingShiftAmount);

        // Tutor preference
        TutorPreferences tutorPreferences = new TutorPreferences(coursePreferences, shiftPreferences, shiftFrequencyPreference);

        // TutorStatus
        TutorStatus tutorStatus = TutorStatus.fromULCRole(this.role);

        return new Tutor(id, firstName, lastName, tutorPreferences, tutorStatus);
    }
}
