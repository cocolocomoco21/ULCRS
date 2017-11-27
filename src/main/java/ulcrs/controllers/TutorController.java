package ulcrs.controllers;

import com.google.gson.annotations.Expose;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import spark.RouteGroup;
import ulcrs.data.DataStore;
import ulcrs.models.course.Course;
import ulcrs.models.tutor.Tutor;

import java.util.List;

import static spark.Spark.before;
import static spark.Spark.get;

public class TutorController extends BaseController {

    @Override
    public RouteGroup routes() {
        return () -> {
            before("/*", (request, response) -> log.info("endpoint: " + request.pathInfo()));
            get("/", this::getTutorList,  tutors -> {
                // Return only the required fields in JSON response
                Gson gson = new GsonBuilder()
                        .addSerializationExclusionStrategy(new ExclusionStrategy() {
                            @Override
                            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                                final Expose expose = fieldAttributes.getAnnotation(Expose.class);
                                // Skip "courseRequirements" field in tutor list deserialization
                                return expose == null
                                        || !expose.serialize()
                                        || (fieldAttributes.getDeclaringClass() == Course.class && fieldAttributes.getName().equals("courseRequirements"));
                            }

                            @Override
                            public boolean shouldSkipClass(Class<?> clazz) {
                                return false;
                            }
                        })
                        .setPrettyPrinting()
                        .create();
            	return gson.toJson(tutors);
            });
            get("/:id", this::getTutor, gson::toJson);
        };
    }

    private List<Tutor> getTutorList(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);
        String cookie = request.headers("Set-Cookie");
        return DataStore.getTutors(cookie);
    }

    private Tutor getTutor(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);
        int id = Integer.valueOf(request.params("id"));
        String cookie = request.headers("Set-Cookie");
        return DataStore.getTutor(id, cookie);
    }
}
