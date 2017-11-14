package ulcrs.controllers;

import spark.Request;
import spark.Response;
import spark.RouteGroup;
import ulcrs.data.DataStore;
import ulcrs.models.tutor.Tutor;

import java.util.List;

import static spark.Spark.before;
import static spark.Spark.get;

public class TutorController extends BaseController {

    @Override
    public RouteGroup routes() {
        return () -> {
            before("/*", (request, response) -> log.info("endpoint: " + request.pathInfo()));
            get("/", this::getTutorList, gson::toJson);
            get("/:id", this::getTutor, gson::toJson);
        };
    }

    private List<Tutor> getTutorList(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);
        return DataStore.getTutors();
    }

    private Tutor getTutor(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);
        int id = Integer.valueOf(request.params("id"));
        return DataStore.getTutor(id);
    }
}
