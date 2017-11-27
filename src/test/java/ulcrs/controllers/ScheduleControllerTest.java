package ulcrs.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import spark.Request;
import spark.Response;

public class ScheduleControllerTest {

    private ScheduleController scheduleControllerTest;

    @Mock
    private Request requestMock;

    @Mock
    private Response responseMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        scheduleControllerTest = new ScheduleController();
    }

    @Test
    public void successGenerateSchedule() throws Exception {
        boolean generateScheduleResult = Whitebox.invokeMethod(scheduleControllerTest,
                "generateSchedule", requestMock, responseMock);
        // TODO: implement test case
        Assert.assertEquals(true, generateScheduleResult);
    }

    @Test
    public void successValidateSchedule() throws Exception {
        boolean validateScheduleResult = Whitebox.invokeMethod(scheduleControllerTest,
                "validateSchedule", requestMock, responseMock);
        // TODO: implement test case
        Assert.assertEquals(false, validateScheduleResult);
    }
}
