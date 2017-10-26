package ulcrs.controllers;

import static spark.Spark.get;

import java.util.Arrays;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.RouteGroup;

import ulcrs.models.tutor.Tutor;
import ulcrs.models.tutor.TutorPreferences;
import ulcrs.models.tutor.TutorStatus;

public class TutorController implements BaseController {

    public static RouteGroup routes() {
        return () -> {
            get("", TutorController::getTutorList, gson::toJson);
            get("/:id", TutorController::getTutor, gson::toJson);
        };
    }

    private static List<Tutor> getTutorList(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);
        return Arrays.asList(
                new Tutor(4, "Bilbo", "Baggins", new TutorPreferences(), TutorStatus.ACTIVE),
                new Tutor(9, "Spicy", "Memelord", new TutorPreferences(), TutorStatus.GHOST)
        );
    }

    private static Tutor getTutor(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);
        return new Tutor(Integer.valueOf(request.params(":id")), "Bilbo", "Baggins", new TutorPreferences(), TutorStatus.INACTIVE);
    }
}
