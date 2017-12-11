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
        return this.coursePreferences.entrySet()
                .stream()
                .filter(entry -> Arrays.asList(Rank.PREFER, Rank.WILLING).contains(entry.getKey()))
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Return all shifts that are possible for the tutor, i.e. all shifts with Rank=Prefered or Willing.
     */
    public Set<Shift> findPossibleShifts() {
        return this.shiftPreferences.entrySet()
                .stream()
                .filter(entry -> Arrays.asList(Rank.PREFER, Rank.WILLING).contains(entry.getKey()))
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toSet());
    }

    public int findPossibleShiftFrequency() {
        Integer possibleShiftAmount = this.shiftFrequencyPreferences.get(Rank.WILLING);
        if (possibleShiftAmount == null) {
            possibleShiftAmount = 0;
        }
        return possibleShiftAmount;
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
