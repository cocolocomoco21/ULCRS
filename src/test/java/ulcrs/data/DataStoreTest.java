package ulcrs.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ulcrs.models.course.Course;
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
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DataStore.class})
public class DataStoreTest {

    @Test
    public void testPopulateData_formsCorrectSizes() {
        List<String> mockResponse = getMockResponseFromJson();

        // Verify populateData succeeded
        assertThat(DataStore.populateData(mockResponse), is(true));

        // Verify fetch happened and saved data correctly
        assertThat(DataStore.getCourses("").size(), is(65));
        assertThat(DataStore.getTutors("").size(), is(27));
        assertThat(DataStore.getShifts("").size(), is(7));
    }

    @Test
    public void testPopulateData_courseCorrectlyFormed() {
        List<String> mockResponse = getMockResponseFromJson();
        DataStore.populateData(mockResponse);

        // Verify correctly formed Course
        Course course = DataStore.getCourse(2673, "");
        assertThat(course.getId(), is(2673));
        assertThat(course.getName(), is("CBE 310"));
        assertThat(course.getCourseRequirements().getRequiredShifts().size(), is(0));
    }

    @Test
    public void testPopulateData_shiftCorrectlyFormed() {
        List<String> mockResponse = getMockResponseFromJson();
        DataStore.populateData(mockResponse);

        // Verify correctly formed Shift
        Shift shift = DataStore.getShift(2, "");
        assertThat(shift.getId(), is(2));
        assertThat(shift.getDay(), is(DayOfWeek.SUNDAY));
        assertThat(shift.getStartTime(), is(LocalTime.of(0, 0)));
        assertThat(shift.getEndTime(), is(LocalTime.of(23, 0)));
    }

    @Test
    public void testPopulateData_tutorCorrectlyFormed() {
        List<String> mockResponse = getMockResponseFromJson();
        DataStore.populateData(mockResponse);

        // Verify correctly formed Tutor
        Tutor tutor = DataStore.getTutor(788586, "");
        assertThat(tutor.getId(), is(788586));
        assertThat(tutor.getFirstName(), is("Darth"));
        assertThat(tutor.getLastName(), is("Vader"));
        assertThat(tutor.getTutorPreferences().getCoursePreferences().get(Rank.PREFER), is(Collections.emptySet()));
        assertThat(tutor.getTutorStatus(), is(TutorStatus.ACTIVE));
    }

    @Test
    public void testPopulateData_sizeNot1() {
        List<String> mockResponse = new ArrayList<>();

        // Fail for empty response array
        assertThat(DataStore.populateData(mockResponse), is(false));

        mockResponse.add("line 1");
        mockResponse.add("line 2");

        // Fail for more than one String in response array
        assertThat(DataStore.populateData(mockResponse), is(false));
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
