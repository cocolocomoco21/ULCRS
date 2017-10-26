package controllers;

import static spark.Spark.get;
import spark.Request;
import spark.Response;
import spark.RouteGroup;

import static spark.Spark.path;

public class TutorController {

    public static RouteGroup routes() {
        return () -> {
            get("/test", (request, response) -> "test output");
            get("/hello", (request, response) -> "HEY");
        };
    }

    public static String handle(Request request, Response response) {
        return "TUTOR yo";
    }
}
