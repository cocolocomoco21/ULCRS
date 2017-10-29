package ulcrs.controllers;

import static spark.Spark.get;

import java.util.Arrays;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.RouteGroup;
import ulcrs.models.course.Course;
import ulcrs.models.course.CourseIntensity;
import ulcrs.models.course.CourseRequirements;

public class CourseController extends BaseController {

    @Override
    public RouteGroup routes() {
        return () -> {
            get("", this::getCourseList, gson::toJson);
            get("/:id", this::getCourse, gson::toJson);
        };
    }

    private List<Course> getCourseList(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);
        return Arrays.asList(
                new Course(4, "CS 301", new CourseRequirements(null, 3, 4, CourseIntensity.HIGH)),
                new Course(16, "CS 302", new CourseRequirements(null, 8, 12, CourseIntensity.HIGH)),
                new Course(13, "CS 577", new CourseRequirements(null, 2, 3, CourseIntensity.LOW)));
    }

    private Course getCourse(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);
        return new Course(Integer.valueOf(request.params("id")), "CS 302", new CourseRequirements( null, 8, 12, CourseIntensity.HIGH));
    }
}
