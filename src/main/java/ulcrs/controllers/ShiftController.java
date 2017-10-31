package ulcrs.controllers;

import spark.Request;
import spark.Response;
import spark.RouteGroup;
import spark.Spark;
import ulcrs.models.shift.Shift;

import java.time.DayOfWeek;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

public class ShiftController extends BaseController {

    @Override
    public RouteGroup routes() {
        return () -> {
            Spark.before("/*", (request, response) -> log.info("endpoint: " + request.pathInfo()));
            Spark.get("/", this::getShiftList, gson::toJson);
        };
    }

    private List<Shift> getShiftList(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        // TODO implement

        OffsetTime startTime = OffsetTime.of(6, 30, 0, 0, ZoneOffset.UTC);
        OffsetTime endTime = OffsetTime.of(9, 0, 0, 0, ZoneOffset.UTC);

        return Arrays.asList(
                new Shift(0, DayOfWeek.SUNDAY, startTime, endTime),
                new Shift(1, DayOfWeek.MONDAY, startTime, endTime),
                new Shift(2, DayOfWeek.TUESDAY, startTime, endTime),
                new Shift(3, DayOfWeek.WEDNESDAY, startTime, endTime),
                new Shift(4, DayOfWeek.THURSDAY, startTime, endTime));
    }
}
