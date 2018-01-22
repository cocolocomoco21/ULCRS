package ulcrs.controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import spark.Request;
import spark.Response;
import spark.RouteGroup;
import ulcrs.models.schedule.Schedule;
import ulcrs.scheduler.SchedulerHelper;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;

public class ScheduleController extends BaseController {

    private static final int DEFAULT_TIME_LIMIT_IN_SECOND = 5;
    private static final int DEFAULT_SOLUTION_LIMIT = 5;

    @Override
    public RouteGroup routes() {
        return () -> {
            before("/*", (request, response) -> log.info("endpoint: " + request.pathInfo()));
            get("/", this::fetchGeneratedSchedules, gson::toJson);
            post("/generate", this::generateSchedule, gson::toJson);
            post("/validate", this::validateSchedule, gson::toJson);
        };
    }

    private List<Schedule> fetchGeneratedSchedules(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);
        return SchedulerHelper.fetchGeneratedSchedules();
    }

    private boolean generateSchedule(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        int timeLimitInSecond = DEFAULT_TIME_LIMIT_IN_SECOND;
        String timeLimitInSecondParam = request.queryParams("timeLimitInSecond");
        if (timeLimitInSecondParam != null) {
            timeLimitInSecond = Integer.parseInt(timeLimitInSecondParam);
        }
        log.info("timeLimitInSecond: " + timeLimitInSecondParam);

        int solutionLimit = DEFAULT_SOLUTION_LIMIT;
        String solutionLimitParam = request.queryParams("solutionLimit");
        if (solutionLimitParam != null) {
            solutionLimit = Integer.parseInt(solutionLimitParam);
        }
        log.info("solutionLimit: " + solutionLimitParam);

        List<Integer> excludedIds = new ArrayList<>();
        String body = request.body();
        JsonObject excludedIdsJsonObj = gson.fromJson(body, JsonObject.class);
        if (excludedIdsJsonObj != null) {
            JsonElement excludedIdsJson = excludedIdsJsonObj.get("excludedIds");
            if (excludedIds.size() != 0) {
                excludedIdsJson.getAsJsonArray().forEach(element -> {
                    excludedIds.add(element.getAsInt());
                });
            }
        }

        return SchedulerHelper.generateSchedule(timeLimitInSecond, solutionLimit);
    }

    private boolean validateSchedule(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        // TODO implement
        Schedule schedule = gson.fromJson(request.body(), Schedule.class);
        return SchedulerHelper.verifySchedule(schedule);
    }
}
