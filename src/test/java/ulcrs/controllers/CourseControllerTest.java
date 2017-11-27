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
import ulcrs.models.course.Course;

import java.util.ArrayList;
import java.util.List;

import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CourseController.class, DataStore.class})
public class CourseControllerTest {

    private CourseController courseControllerTest;

    @Mock
    private Request requestMock;

    @Mock
    private Response responseMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(DataStore.class);
        courseControllerTest = new CourseController();
    }

    @Test
    public void successGetCourseList() throws Exception {
        List<Course> courseListTest = new ArrayList<>();

        when(requestMock.headers("Set-Cookie")).thenReturn("cookie");
        when(DataStore.getCourses("cookie")).thenReturn(courseListTest);

        List<Course> getCourseListResult = Whitebox.invokeMethod(courseControllerTest, "getCourseList", requestMock, responseMock);
        Assert.assertEquals(courseListTest, getCourseListResult);
    }

    @Test
    public void successGetCourse() throws Exception {
        List<Course> courseListTest = new ArrayList<>();
        courseListTest.add(new Course(1, "a", null));
        courseListTest.add(new Course(2, "b", null));

        when(requestMock.headers("Set-Cookie")).thenReturn("cookie");
        when(DataStore.getCourse(1, "cookie")).thenReturn(courseListTest.get(0));
        when(requestMock.params(Mockito.eq("id"))).thenReturn("1");

        Course getCourseResult = Whitebox.invokeMethod(courseControllerTest, "getCourse", requestMock, responseMock);
        Assert.assertEquals(courseListTest.get(0), getCourseResult);
    }
}
