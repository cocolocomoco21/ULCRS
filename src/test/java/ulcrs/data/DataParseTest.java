package ulcrs.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ulcrs.models.course.Course;
import ulcrs.models.course.CourseIntensity;
import ulcrs.models.rank.Rank;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;
import ulcrs.models.tutor.TutorStatus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DataParse.class})
public class DataParseTest {
    @Test
    public void testParse_formsCorrectSizes() {
        JsonObject mockObject = formMockJson();

        // Verify parse succeeded
        ParsedULCResponse response = DataParse.parse(mockObject);
        assertThat(response.getCourses().isEmpty(), is(false));
        assertThat(response.getShifts().isEmpty(), is(false));
        assertThat(response.getTutors().isEmpty(), is(false));
    }

    @Test
    public void testParse_courseCorrectlyFormed() {
        JsonObject mockObject = formMockJson();
        ParsedULCResponse response = DataParse.parse(mockObject);

        // Verify correctly formed Course
        Course course = response.getCourses().get(0);
        assertThat(course.getId(), is(2122));
        assertThat(course.getName(), is("ISYE 313"));
        assertThat(course.getCourseRequirements().getRequiredShifts().size(), is(0));
        assertThat(course.getCourseRequirements().getIntensity(), is(CourseIntensity.LOW));
        assertThat(course.getCourseRequirements().getPreferredShiftAmount(), is(0));
        assertThat(course.getCourseRequirements().getRequiredShiftAmount(), is(3));
        assertThat(course.getCourseRequirements().getNumTutorsPerShift().size(), is(0));
        assertThat(course.getCourseRequirements().getNumTutorsPerWeek(), is(1));
    }

    @Test
    public void testParse_shiftCorrectlyFormed() {
        JsonObject mockObject = formMockJson();
        ParsedULCResponse response = DataParse.parse(mockObject);

        // Verify correctly formed Shift
        Shift shift = response.getShifts().get(0);
        assertThat(shift.getId(), is(1));
        assertThat(shift.getDay(), is(DayOfWeek.MONDAY));
        assertThat(shift.getStartTime(), is(LocalTime.of(18, 30)));
        assertThat(shift.getEndTime(), is(LocalTime.of(21, 0)));
    }

    @Test
    public void testParse_tutorCorrectlyFormed() {
        JsonObject mockObject = formMockJson();
        ParsedULCResponse response = DataParse.parse(mockObject);

        // Verify correctly formed Tutor
        Tutor tutor = response.getTutors().get(0);  // Get at index 1 since Tutor at index 0 has no meaningful attributes
        assertThat(tutor.getId(), is(4850785));
        assertThat(tutor.getFirstName(), is("Luke"));
        assertThat(tutor.getLastName(), is("Skywalker"));

        assertThat(tutor.getTutorPreferences().getCoursePreferences().get(Rank.PREFER).stream().map(Course::getId).collect(Collectors.toSet()),
                is(Sets.newSet(11995, 11996)));
        assertThat(tutor.getTutorPreferences().getCoursePreferences().get(Rank.WILLING).stream().map(Course::getId).collect(Collectors.toSet()),
                is(Sets.newSet(2749, 2750, 11991, 15599, 15600, 15602, 15606)));

        assertThat(tutor.getTutorPreferences().getShiftPreferences().get(Rank.PREFER).stream().map(Shift::getId).collect(Collectors.toSet()),
                is(Sets.newSet(5)));
        assertThat(tutor.getTutorPreferences().getShiftPreferences().get(Rank.WILLING).stream().map(Shift::getId).collect(Collectors.toSet()),
                is(Sets.newSet(1, 2)));

        assertThat(tutor.getTutorPreferences().getShiftFrequencyPreferences().get(Rank.PREFER), is(1));
        assertThat(tutor.getTutorPreferences().getShiftFrequencyPreferences().get(Rank.WILLING), is(3));

        assertThat(tutor.getTutorStatus(), is(TutorStatus.ACTIVE));
    }

    private static JsonObject formMockJson() {
        // Read mock data file
        InputStream inputStream = DataStore.class.getClassLoader().getResourceAsStream("mockULCResponse.json");
        String response = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));

        return new Gson().fromJson(response, JsonObject.class);
    }

}
