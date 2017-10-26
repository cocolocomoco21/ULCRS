package ulcrs.controllers;

import com.google.gson.Gson;
import spark.RouteGroup;

public abstract class BaseController {
    static String CONTENT_TYPE_JSON = "application/json";

    Gson gson = new Gson();

    abstract RouteGroup routes();
}
