package ulcrs.controllers;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import spark.Request;
import spark.Response;
import spark.RouteGroup;
import spark.Spark;
import ulcrs.models.tutor.Tutor;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class TutorController extends BaseController {

    @Override
    public RouteGroup routes() {
        return () -> {
            Spark.before("/*", (request, response) -> log.info("endpoint: " + request.pathInfo()));
            Spark.get("", this::getTutorList, gson::toJson);
            Spark.get("/:id", this::getTutor, gson::toJson);
        };
    }

    private List<Tutor> getTutorList(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        // TODO implement
        // Fetch from either memory or call to ULC server

        InputStream is = getClass().getClassLoader().getResourceAsStream("mockTutorData.json");

        JsonReader reader = new JsonReader(new InputStreamReader(is));
        List<Tutor> tutors = gson.fromJson(reader, new TypeToken<List<Tutor>>() {
        }.getType());

        return tutors;
    }

    private Tutor getTutor(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        // TODO implement
        // Fetch from either memory or call to ULC server

        int id = Integer.valueOf(request.params(":id"));
        List<Tutor> tutors = getTutorList(request, response);

        return tutors.stream().findFirst().filter(tutor -> tutor.getId() == id).orElse(null);
    }
}
