package ulcrs.controllers;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import spark.Request;
import spark.Response;
import spark.RouteGroup;
import ulcrs.models.course.Course;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static spark.Spark.before;
import static spark.Spark.get;

public class CourseController extends BaseController {

    @Override
    public RouteGroup routes() {
        return () -> {
            before("/*", (request, response) -> log.info("endpoint: " + request.pathInfo()));
            get("/", this::getCourseList, gson::toJson);
            get("/:id", this::getCourse, gson::toJson);
        };
    }

    private List<Course> getCourseList(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        InputStream is = getClass().getClassLoader().getResourceAsStream("mockCoursesNoTime.json");
        JsonReader reader = new JsonReader(new InputStreamReader(is));

        List<Course> courses = gson.fromJson(reader, new TypeToken<List<Course>>() {
        }.getType());
        return courses;
    }

    private Course getCourse(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        List<Course> courses = getCourseList(request, response);
        int id = Integer.valueOf(request.params("id"));

        return courses.stream()
                .filter(course -> course.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
