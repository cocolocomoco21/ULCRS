package ulcrs.controllers;

import com.google.gson.Gson;
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
import ulcrs.models.tutor.Tutor;

import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TutorController.class, Gson.class})
public class TutorControllerTest {

    private TutorController tutorControllerTest;

    @Mock
    private Request requestMock;

    @Mock
    private Response responseMock;

    private Gson gsonMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        tutorControllerTest = new TutorController();

        gsonMock = PowerMockito.mock(Gson.class);
        PowerMockito.mockStatic(BaseController.class);
        Whitebox.setInternalState(BaseController.class, "gson", gsonMock);
    }

    @Test
    public void successGetTutorList() throws Exception {
        List<Tutor> tutorListTest = new ArrayList<>();
        Mockito.when(gsonMock.fromJson(Mockito.any(JsonReader.class), Mockito.any())).thenReturn(tutorListTest);

        List<Tutor> getTutorListResult = Whitebox.invokeMethod(tutorControllerTest, "getTutorList",
                requestMock, responseMock);
        Assert.assertEquals(tutorListTest, getTutorListResult);
    }

    @Test
    public void successGetTutor() throws Exception {
        List<Tutor> tutorListTest = new ArrayList<>();
        tutorListTest.add(new Tutor(1, "d", "s", null, null));
        tutorListTest.add(new Tutor(2, "d", "s", null, null));

        Mockito.when(gsonMock.fromJson(Mockito.any(JsonReader.class), Mockito.any())).thenReturn(tutorListTest);
        Mockito.when(requestMock.params(Mockito.eq("id"))).thenReturn("1");

        Tutor getTutorResult = Whitebox.invokeMethod(tutorControllerTest, "getTutor",
                requestMock, responseMock);
        Assert.assertEquals(tutorListTest.get(0), getTutorResult);
    }
}
