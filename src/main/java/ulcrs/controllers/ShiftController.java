package ulcrs.controllers;

import spark.Request;
import spark.Response;
import spark.RouteGroup;
import ulcrs.models.shift.Shift;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static spark.Spark.before;
import static spark.Spark.get;

public class ShiftController extends BaseController {

    @Override
    public RouteGroup routes() {
        return () -> {
            before("/*", (request, response) -> log.info("endpoint: " + request.pathInfo()));
            get("/", this::getShiftList, gson::toJson);
        };
    }

    private List<Shift> getShiftList(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        // TODO implement

        LocalTime startTime = LocalTime.of(6, 30);
        LocalTime endTime = LocalTime.of(9, 0);

        return Arrays.asList(
                new Shift(0, DayOfWeek.SUNDAY, startTime, endTime),
                new Shift(1, DayOfWeek.MONDAY, startTime, endTime),
                new Shift(2, DayOfWeek.TUESDAY, startTime, endTime),
                new Shift(3, DayOfWeek.WEDNESDAY, startTime, endTime),
                new Shift(4, DayOfWeek.THURSDAY, startTime, endTime));
    }
}
