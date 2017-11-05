package ulcrs.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import spark.Request;
import spark.Response;
import ulcrs.models.shift.Shift;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class ShiftControllerTest {

    private ShiftController shiftControllerTest;

    @Mock
    private Request requestMock;

    @Mock
    private Response responseMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        shiftControllerTest = new ShiftController();
    }

    @Test
    public void successGetShiftList() throws Exception {
        List<Shift> getShiftListResult = Whitebox.invokeMethod(shiftControllerTest, "getShiftList",
                requestMock, responseMock);

        LocalTime startTime = LocalTime.of(6, 30);
        LocalTime endTime = LocalTime.of(9, 0);
        List<Shift> shiftListTest = Arrays.asList(
                new Shift(0, DayOfWeek.SUNDAY, startTime, endTime),
                new Shift(1, DayOfWeek.MONDAY, startTime, endTime),
                new Shift(2, DayOfWeek.TUESDAY, startTime, endTime),
                new Shift(3, DayOfWeek.WEDNESDAY, startTime, endTime),
                new Shift(4, DayOfWeek.THURSDAY, startTime, endTime));

        Assert.assertEquals(shiftListTest, getShiftListResult);
    }
}
