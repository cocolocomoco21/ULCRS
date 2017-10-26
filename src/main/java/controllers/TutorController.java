package controllers;

import static spark.Spark.get;

import java.util.Arrays;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.RouteGroup;

public class TutorController implements BaseController {

    public static RouteGroup routes() {
        return () -> {
            get("", TutorController::getTutorList);
            get("/:id", TutorController::getTutor);
        };
    }

    private static List<String> getTutorList(Request request, Response response) {
        return Arrays.asList("This would give a list of tutors, like so:", "1", "14", "3");
    }

    private static String getTutor(Request request, Response response) {
        return "this would give details on a specific tutor (i.e. tutor id = " + request.params(":id") + ")";
    }
}
