package ulcrs.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import spark.Request;
import spark.Response;
import ulcrs.GsonFactory;
import ulcrs.models.schedule.Schedule;
import ulcrs.models.session.Session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SessionController.class)
public class SessionControllerTest {

    private static final String WORKSPACE_PATH = "../sessions/";
    private static final String TEST_WORKSPACE_PATH = "src/test/resources/sessions/";

    private SessionController sessionControllerTest;

    private Gson gson = GsonFactory.getGson();

    @Mock
    private Request requestMock;

    @Mock
    private Response responseMock;

    private File directoryTest;
    private FileReader fileReaderTest;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);

        sessionControllerTest = new SessionController();

        directoryTest = new File(TEST_WORKSPACE_PATH);
        fileReaderTest = new FileReader(TEST_WORKSPACE_PATH + "2.json");

        Mockito.when(requestMock.params(Mockito.eq(":name"))).thenReturn("2");

        InputStream is = SessionController.class.getClassLoader().getResourceAsStream("mockSessionSchedule.json");
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        Schedule schedule = gson.fromJson(reader, new TypeToken<Schedule>() {
        }.getType());
        String scheduleStrTest = gson.toJson(schedule);
        Mockito.when(requestMock.body()).thenReturn(scheduleStrTest);

        PowerMockito.whenNew(File.class).withParameterTypes(String.class)
                .withArguments(Mockito.eq(WORKSPACE_PATH)).thenReturn(directoryTest);
        PowerMockito.whenNew(FileReader.class).withParameterTypes(String.class)
                .withArguments(Mockito.eq(WORKSPACE_PATH + "2.json")).thenReturn(fileReaderTest);
    }

    @Test
    public void successGetSessionList() throws Exception {
        List<String> getSessionListResult = Whitebox.invokeMethod(sessionControllerTest, "getSessionList",
                requestMock, responseMock);

        PowerMockito.verifyNew(File.class).withArguments(Mockito.eq(WORKSPACE_PATH));

        List<String> expectedFilenameList = new ArrayList<>();
        expectedFilenameList.add("session_1511824120.json");
        expectedFilenameList.add("session_1511824113.json");
        expectedFilenameList.add("2.json");
        expectedFilenameList.add("1.json");

        Assert.assertEquals(expectedFilenameList, getSessionListResult);
    }

    @Test
    public void failGetSession() throws Exception {
        Mockito.when(requestMock.params(Mockito.eq(":name"))).thenReturn("3.json");
        PowerMockito.whenNew(FileReader.class).withParameterTypes(String.class)
                .withArguments(Mockito.eq(WORKSPACE_PATH + "3.json")).thenThrow(new FileNotFoundException());

        Session getSessionResult = Whitebox.invokeMethod(sessionControllerTest, "getSession",
                requestMock, responseMock);

        Assert.assertEquals(null, getSessionResult);
    }

    @Test
    public void successGetSession() throws Exception {
        Session getSessionResult = Whitebox.invokeMethod(sessionControllerTest, "getSession",
                requestMock, responseMock);

        PowerMockito.verifyNew(FileReader.class).withArguments(Mockito.eq(WORKSPACE_PATH + "2.json"));

        Assert.assertEquals("2.json", getSessionResult.getName());
    }

    @Test
    public void successSaveUnnamedSession() throws Exception {
        // TODO: implement test case
        Mockito.when(requestMock.params(Mockito.eq(":name"))).thenReturn(null);
        Boolean saveSessionResult = Whitebox.invokeMethod(sessionControllerTest, "saveSession",
                requestMock, responseMock);
        Assert.assertEquals(false, saveSessionResult);
    }

    @Test
    public void successSaveNamedSession() throws Exception {
        // TODO: implement test case
        Mockito.when(requestMock.params(Mockito.eq(":name"))).thenReturn("");
        Boolean saveSessionResult = Whitebox.invokeMethod(sessionControllerTest, "saveSession",
                requestMock, responseMock);
        Assert.assertEquals(false, saveSessionResult);
    }

    @Test(expected = NullPointerException.class)
    public void successDeleteSession() throws Exception {
        // TODO: implement test case
        Boolean saveSessionResult = Whitebox.invokeMethod(sessionControllerTest, "saveSession",
                requestMock, responseMock);
        Boolean deleteSessionResult = Whitebox.invokeMethod(sessionControllerTest, "deleteSession",
                requestMock, responseMock);
    }
}
