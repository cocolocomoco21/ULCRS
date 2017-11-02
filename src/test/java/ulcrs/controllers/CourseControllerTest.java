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
import ulcrs.models.course.Course;

import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CourseController.class, Gson.class})
public class CourseControllerTest {

    private CourseController courseControllerTest;

    @Mock
    private Request requestMock;

    @Mock
    private Response responseMock;

    private Gson gsonMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        courseControllerTest = new CourseController();

        gsonMock = PowerMockito.mock(Gson.class);
        PowerMockito.mockStatic(BaseController.class);
        Whitebox.setInternalState(BaseController.class, "gson", gsonMock);
    }

    @Test
    public void successGetCourseList() throws Exception {
        List<Course> courseListTest = new ArrayList<>();
        Mockito.when(gsonMock.fromJson(Mockito.any(JsonReader.class), Mockito.any())).thenReturn(courseListTest);

        List<Course> getCourseListResult = Whitebox.invokeMethod(courseControllerTest, "getCourseList",
                requestMock, responseMock);
        Assert.assertEquals(courseListTest, getCourseListResult);
    }

    @Test
    public void successGetCourse() throws Exception {
        List<Course> courseListTest = new ArrayList<>();
        courseListTest.add(new Course(1, "a", null));
        courseListTest.add(new Course(2, "b", null));

        Mockito.when(gsonMock.fromJson(Mockito.any(JsonReader.class), Mockito.any())).thenReturn(courseListTest);
        Mockito.when(requestMock.params(Mockito.eq("id"))).thenReturn("1");

        Course getCourseResult = Whitebox.invokeMethod(courseControllerTest, "getCourse",
                requestMock, responseMock);
        Assert.assertEquals(courseListTest.get(0), getCourseResult);
    }
}
