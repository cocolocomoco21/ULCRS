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
    public void testPopulateData_correctlyUpdates() {
        List<String> mockResponse = getMockResponseFromJson();

        // Verify empty before update
        assertThat(DataStore.getCourses("").isEmpty(), is(true));
        assertThat(DataStore.getTutors("").isEmpty(), is(true));
        assertThat(DataStore.getShifts("").isEmpty(), is(true));

        // Verify populateData succeeded
        assertThat(DataStore.populateData(mockResponse), is(true));

        // Verify fetch happened and saved data correctly
        assertThat(DataStore.getCourses("").isEmpty(), is(false));
        assertThat(DataStore.getTutors("").isEmpty(), is(false));
        assertThat(DataStore.getShifts("").isEmpty(), is(false));
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
