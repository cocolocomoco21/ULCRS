package ulcrs.controllers;

import com.google.gson.Gson;
import spark.RouteGroup;

public interface BaseController {
    String CONTENT_TYPE_JSON = "application/json";

    Gson gson = new Gson();

    static RouteGroup routes() {
        return null;    // Default implementation since static methods in interface require default implementation
    }
}
