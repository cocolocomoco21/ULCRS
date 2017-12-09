package ulcrs.data;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DataParse.class})
public class DataParseTest {
    @Test
    public void testPopulateData_formsCorrectSizes() {
        List<String> mockResponse = getMockResponseFromJson();

        // Verify populateData succeeded
        assertThat(DataStore.populateData(mockResponse), is(true));

        // Verify fetch happened and saved data correctly
        assertThat(DataStore.getCourses("").size(), is(69));
        assertThat(DataStore.getTutors("").size(), is(49));
        assertThat(DataStore.getShifts("").size(), is(5));
    }

    @Test
    public void testPopulateData_courseCorrectlyFormed() {
        List<String> mockResponse = getMockResponseFromJson();
        DataStore.populateData(mockResponse);

        // Verify correctly formed Course
        Course course = DataStore.getCourse(2122, "");
        assertThat(course.getId(), is(2122));
        assertThat(course.getName(), is("ISYE 313"));
        assertThat(course.getCourseRequirements().getRequiredShifts().size(), is(0));
        assertThat(course.getCourseRequirements().getIntensity(), is(CourseIntensity.MEDIUM));
        assertThat(course.getCourseRequirements().getPreferredShiftAmount(), is(0));
        assertThat(course.getCourseRequirements().getRequiredShiftAmount(), is(0));
    }

    @Test
    public void testPopulateData_shiftCorrectlyFormed() {
        List<String> mockResponse = getMockResponseFromJson();
        DataStore.populateData(mockResponse);

        // Verify correctly formed Shift
        Shift shift = DataStore.getShift(1, "");
        assertThat(shift.getId(), is(1));
        assertThat(shift.getDay(), is(DayOfWeek.SUNDAY));
        assertThat(shift.getStartTime(), is(LocalTime.of(18, 30)));
        assertThat(shift.getEndTime(), is(LocalTime.of(21, 0)));
    }

    @Test
    public void testPopulateData_tutorCorrectlyFormed() {
        List<String> mockResponse = getMockResponseFromJson();
        DataStore.populateData(mockResponse);

        // Verify correctly formed Tutor
        Tutor tutor = DataStore.getTutor(4850785, "");
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

    private static List<String> getMockResponseFromJson() {
        InputStream inputStream = DataStore.class.getClassLoader().getResourceAsStream("mockULCResponse.json");

        // Read mock data file
        String response = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));

        // Make response
        List<String> mockResponse = new ArrayList<>();
        mockResponse.add(response);

        return mockResponse;
    }

}
