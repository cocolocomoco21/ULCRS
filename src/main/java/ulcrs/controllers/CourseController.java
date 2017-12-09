package ulcrs.controllers;

import spark.Request;
import spark.Response;
import spark.RouteGroup;
import ulcrs.data.DataStore;
import ulcrs.models.course.Course;

import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.before;
import static spark.Spark.get;

public class CourseController extends BaseController {

    @Override
    public RouteGroup routes() {
        return () -> {
            before("/*", (request, response) -> log.info("endpoint: " + request.pathInfo()));
            get("/", this::getCourseList, exposeOnlyGson::toJson);
            get("/:id", this::getCourse, gson::toJson);
        };
    }

    private List<Course> getCourseList(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        String cookie = request.headers("Set-Cookie");
        String limitParam = request.queryParamOrDefault("limit", null);

        List<Course> courses = DataStore.getCourses(cookie);

        // Apply limit if it exists
        if (limitParam != null && !courses.isEmpty()) {
            int limit = Integer.parseUnsignedInt(limitParam);
            courses = courses.stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        return courses;
    }

    private Course getCourse(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        int id = Integer.valueOf(request.params(":id"));
        String cookie = request.headers("Set-Cookie");

        return DataStore.getCourse(id, cookie);
    }
}
