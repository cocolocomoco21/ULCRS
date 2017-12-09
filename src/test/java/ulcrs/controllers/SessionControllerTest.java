package ulcrs.controllers;

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
import ulcrs.models.session.Session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SessionController.class)
public class SessionControllerTest {

    private static final String WORKSPACE_PATH = "../sessions/";
    private static final String TEST_WORKSPACE_PATH = "src/test/resources/sessions/";

    private SessionController sessionControllerTest;

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
    public void successSaveSession() throws Exception {
        // TODO: implement test case
        Assert.assertTrue(true);

//        Boolean updateSessionResult = Whitebox.invokeMethod(sessionControllerTest, "saveSession",
//                requestMock, responseMock);
//        Assert.assertEquals(false, updateSessionResult);
    }

    @Test
    public void successDeleteSession() throws Exception {
        // TODO: implement test case
        Assert.assertTrue(true);

//        Boolean deleteSessionResult = Whitebox.invokeMethod(sessionControllerTest, "deleteSession",
//                requestMock, responseMock);
//        Assert.assertEquals(false, deleteSessionResult);
    }
}
