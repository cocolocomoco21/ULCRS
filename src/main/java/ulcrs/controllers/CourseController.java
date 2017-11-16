package ulcrs.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import spark.RouteGroup;
import ulcrs.data.DataStore;
import ulcrs.models.course.Course;

import java.util.List;

import static spark.Spark.before;
import static spark.Spark.get;

public class CourseController extends BaseController {

    @Override
    public RouteGroup routes() {
        return () -> {
            before("/*", (request, response) -> log.info("endpoint: " + request.pathInfo()));
            get("/", this::getCourseList, courses -> {
            	// Return only the required fields in JSON response
            	Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            	return gson.toJson(courses);	
            });
            get("/:id", this::getCourse, gson::toJson);
        };
    }

    private List<Course> getCourseList(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);
        return DataStore.getCourses();
    }

    private Course getCourse(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);
        int id = Integer.valueOf(request.params("id"));
        return DataStore.getCourse(id);
    }
}
