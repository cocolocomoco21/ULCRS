package ulcrs.models.tutor;

import ulcrs.models.course.Course;
import ulcrs.models.rank.Rank;
import ulcrs.models.shift.Shift;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.annotations.Expose;

public class TutorPreferences {

    @Expose
    private Map<Rank, Set<Course>> coursePreferences;

    @Expose
    private Map<Rank, Set<Shift>> shiftPreferences;

    @Expose
    private Map<Rank, Integer> shiftFrequencyPreferences;

    private Set<Course> possibleCourses;
    private Set<Shift> possibleShifts;
    private Integer possibleShiftAmount;

    public TutorPreferences(Map<Rank, Set<Course>> coursePreferences, Map<Rank, Set<Shift>> shiftPreferences, Map<Rank, Integer> shiftFrequencyPreferences) {
        this.coursePreferences = coursePreferences;
        this.shiftPreferences = shiftPreferences;
        this.shiftFrequencyPreferences = shiftFrequencyPreferences;
    }

    public Map<Rank, Set<Course>> getCoursePreferences() {
        return coursePreferences;
    }

    public void setCoursePreferences(Map<Rank, Set<Course>> coursePreferences) {
        this.coursePreferences = coursePreferences;
    }

    public Map<Rank, Set<Shift>> getShiftPreferences() {
        return shiftPreferences;
    }

    public void setShiftPreferences(Map<Rank, Set<Shift>> shiftPreferences) {
        this.shiftPreferences = shiftPreferences;
    }

    public Map<Rank, Integer> getShiftFrequencyPreferences() {
        return shiftFrequencyPreferences;
    }

    public void setShiftFrequencyPreferences(Map<Rank, Integer> shiftFrequencyPreferences) {
        this.shiftFrequencyPreferences = shiftFrequencyPreferences;
    }

    /**
     * Return all courses that are possible for the tutor, i.e. all Rank=Prefered or Willing.
     */
    public Set<Course> findPossibleCourses() {
        if (this.possibleCourses == null) {
            this.possibleCourses = this.coursePreferences.entrySet()
                    .stream()
                    .filter(entry -> Arrays.asList(Rank.PREFER, Rank.WILLING).contains(entry.getKey()))
                    .flatMap(entry -> entry.getValue().stream())
                    .collect(Collectors.toSet());
        }

        return this.possibleCourses;
    }

    /**
     * Return all shifts that are possible for the tutor, i.e. all shifts with Rank=Prefered or Willing.
     */
    public Set<Shift> findPossibleShifts() {
        if (this.possibleShifts == null) {
            this.possibleShifts = this.shiftPreferences.entrySet()
                    .stream()
                    .filter(entry -> Arrays.asList(Rank.PREFER, Rank.WILLING).contains(entry.getKey()))
                    .flatMap(entry -> entry.getValue().stream())
                    .collect(Collectors.toSet());
        }
        return this.possibleShifts;
    }

    /**
     * Return the possible shift frequency, i.e. the Rank=Willing frequency for the tutor.
     */
    public int findPossibleShiftFrequency() {
        if (this.possibleShiftAmount == null) {
            Integer willingShiftAmount = this.shiftFrequencyPreferences.get(Rank.WILLING);
            this.possibleShiftAmount = willingShiftAmount == null ? 0 : willingShiftAmount;
        }
        return this.possibleShiftAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TutorPreferences that = (TutorPreferences) o;
        return Objects.equals(coursePreferences, that.coursePreferences) &&
                Objects.equals(shiftPreferences, that.shiftPreferences) &&
                Objects.equals(shiftFrequencyPreferences, that.shiftFrequencyPreferences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coursePreferences, shiftPreferences, shiftFrequencyPreferences);
    }
}
