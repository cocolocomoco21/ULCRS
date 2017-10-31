package ulcrs.controllers;

import spark.Request;
import spark.Response;
import spark.RouteGroup;
import spark.Spark;
import ulcrs.models.schedule.Schedule;
import ulcrs.scheduler.Scheduler;

import java.util.List;

public class ScheduleController extends BaseController {

    @Override
    public RouteGroup routes() {
        return () -> {
            Spark.before("/*", (request, response) -> log.info("endpoint: " + request.pathInfo()));
            Spark.get("/generate", this::generateSchedule, gson::toJson);
            Spark.post("/validate", this::validateSchedule, gson::toJson);
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
