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
import ulcrs.data.DataStore;
import ulcrs.models.tutor.Tutor;

import java.util.ArrayList;
import java.util.List;

import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TutorController.class, DataStore.class})
public class TutorControllerTest {

    private TutorController tutorControllerTest;

    @Mock
    private Request requestMock;

    @Mock
    private Response responseMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(DataStore.class);
        tutorControllerTest = new TutorController();
    }

    @Test
    public void successGetTutorList() throws Exception {
        List<Tutor> tutorListTest = new ArrayList<>();

        when(requestMock.headers("Set-Cookie")).thenReturn("cookie");
        when(DataStore.getTutors("cookie")).thenReturn(tutorListTest);

        List<Tutor> getTutorListResult = Whitebox.invokeMethod(tutorControllerTest, "getTutorList", requestMock, responseMock);
        Assert.assertEquals(tutorListTest, getTutorListResult);
    }

    @Test
    public void successGetLimitedTutorList() throws Exception {
        List<Tutor> tutorListTest = new ArrayList<>();
        tutorListTest.add(new Tutor(1, "f", "l", null, null));
        tutorListTest.add(new Tutor(2, "f", "l", null, null));

        when(requestMock.headers("Set-Cookie")).thenReturn("cookie");
        when(requestMock.queryParamOrDefault("limit", null)).thenReturn("1");
        when(DataStore.getTutors("cookie")).thenReturn(tutorListTest);

        List<Tutor> expectedTutorListTest = new ArrayList<>();
        expectedTutorListTest.add(tutorListTest.get(0));

        List<Tutor> getTutorListResult = Whitebox.invokeMethod(tutorControllerTest, "getTutorList", requestMock, responseMock);
        Assert.assertEquals(expectedTutorListTest, getTutorListResult);
    }

    @Test
    public void successGetTutor() throws Exception {
        List<Tutor> tutorListTest = new ArrayList<>();
        tutorListTest.add(new Tutor(1, "d", "s", null, null));
        tutorListTest.add(new Tutor(2, "d", "s", null, null));

        when(requestMock.headers("Set-Cookie")).thenReturn("cookie");
        when(DataStore.getTutor(1, "cookie")).thenReturn(tutorListTest.get(0));
        Mockito.when(requestMock.params(Mockito.eq(":id"))).thenReturn("1");

        Tutor getTutorResult = Whitebox.invokeMethod(tutorControllerTest, "getTutor", requestMock, responseMock);
        Assert.assertEquals(tutorListTest.get(0), getTutorResult);
    }
}
