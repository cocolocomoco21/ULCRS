package ulcrs.controllers;

import static spark.Spark.get;
import static spark.Spark.post;

import java.util.List;
import spark.Request;
import spark.Response;
import spark.RouteGroup;
import ulcrs.models.schedule.Schedule;

public class ScheduleController extends BaseController {

    @Override
    public RouteGroup routes() {
        return () -> {
            get("/generate", this::generateSchedule, gson::toJson);
            post("/validate", this::validateSchedule, gson::toJson);
        };
    }

    private List<Schedule> generateSchedule(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        // TODO implement
        return null;
    }

    private boolean validateSchedule(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        // TODO implement
        return false;
    }
}
