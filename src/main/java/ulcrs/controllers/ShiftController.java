package ulcrs.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import spark.RouteGroup;
import ulcrs.data.DataStore;
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
            get("/", this::getShiftList, shifts -> {
                    // Return only the required fields in JSON response
                    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
                    return gson.toJson(shifts);
            });
        };
    }

    private List<Shift> getShiftList(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);
        String cookie = request.headers("Set-Cookie");
        return DataStore.getShifts(cookie);
    }
}
