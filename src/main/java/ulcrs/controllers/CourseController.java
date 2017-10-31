package ulcrs.controllers;

import spark.Request;
import spark.Response;
import spark.RouteGroup;
import spark.Spark;
import ulcrs.models.course.Course;
import ulcrs.models.course.CourseIntensity;
import ulcrs.models.course.CourseRequirements;

import java.util.Arrays;
import java.util.List;

public class CourseController extends BaseController {

    @Override
    public RouteGroup routes() {
        return () -> {
            Spark.before("/*", (request, response) -> log.info("endpoint: " + request.pathInfo()));
            Spark.get("", this::getCourseList, gson::toJson);
            Spark.get("/:id", this::getCourse, gson::toJson);
        };
    }

    private List<Course> getCourseList(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        // TODO implement
        return Arrays.asList(
                new Course(4, "CS 301", new CourseRequirements(null, 3, 4, CourseIntensity.HIGH)),
                new Course(16, "CS 302", new CourseRequirements(null, 8, 12, CourseIntensity.HIGH)),
                new Course(13, "CS 577", new CourseRequirements(null, 2, 3, CourseIntensity.LOW)));
    }

    private Course getCourse(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        // TODO implement
        return new Course(Integer.valueOf(request.params("id")), "CS 302", new CourseRequirements(null, 8, 12, CourseIntensity.HIGH));
    }
}
