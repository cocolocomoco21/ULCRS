package ulcrs.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import spark.Request;
import spark.Response;
import ulcrs.models.session.Session;

import java.util.List;

public class SessionControllerTest {

    private SessionController sessionControllerTest;

    @Mock
    private Request requestMock;

    @Mock
    private Response responseMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        sessionControllerTest = new SessionController();
    }

    @Test
    public void successGetSessionList() throws Exception {
        List<Session> getSessionListResult = Whitebox.invokeMethod(sessionControllerTest, "getSessionList",
                requestMock, responseMock);
        // TODO: implement test case
        Assert.assertEquals(null, getSessionListResult);
    }

    @Test
    public void successGetSession() throws Exception {
        Session getSessionResult = Whitebox.invokeMethod(sessionControllerTest, "getSession",
                requestMock, responseMock);
        // TODO: implement test case
        Assert.assertEquals(null, getSessionResult);
    }

    @Test
    public void successUpdateSession() throws Exception {
        Boolean updateSessionResult = Whitebox.invokeMethod(sessionControllerTest, "updateSession",
                requestMock, responseMock);
        // TODO: implement test case
        Assert.assertEquals(false, updateSessionResult);
    }

    @Test
    public void successDeleteSession() throws Exception {
        Boolean deleteSessionResult = Whitebox.invokeMethod(sessionControllerTest, "deleteSession",
                requestMock, responseMock);
        // TODO: implement test case
        Assert.assertEquals(false, deleteSessionResult);
    }
}
