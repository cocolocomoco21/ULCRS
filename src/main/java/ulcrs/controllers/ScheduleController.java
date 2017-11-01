package ulcrs.controllers;

import spark.Request;
import spark.Response;
import spark.RouteGroup;
import ulcrs.models.schedule.Schedule;
import ulcrs.scheduler.Scheduler;

import java.util.List;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;

public class ScheduleController extends BaseController {

    @Override
    public RouteGroup routes() {
        return () -> {
            before("/*", (request, response) -> log.info("endpoint: " + request.pathInfo()));
            get("/generate", this::generateSchedule, gson::toJson);
            post("/validate", this::validateSchedule, gson::toJson);
        };
    }

    private List<Schedule> generateSchedule(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        // TODO implement
        return Scheduler.generateSchedule();
    }

    private boolean validateSchedule(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        // TODO implement
        Schedule schedule = gson.fromJson(request.body(), Schedule.class);
        return Scheduler.verifySchedule(schedule);
    }
}
