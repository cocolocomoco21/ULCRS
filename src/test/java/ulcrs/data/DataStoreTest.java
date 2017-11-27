package ulcrs.data;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataStoreTest {

    @Test
    public void testPopulateData() {
        InputStream is = DataStore.class.getClassLoader().getResourceAsStream("mockULCResponse.json");

        // Read mock data file
        String response = new BufferedReader(new InputStreamReader(is))
                .lines().collect(Collectors.joining("\n"));

        // Make
        List<String> mockResponse = new ArrayList<>();
        mockResponse.add(response);

        DataStore.populateData(mockResponse);
    }
}
