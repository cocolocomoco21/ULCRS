package controllers;

import spark.RouteGroup;

public interface BaseController {

    static RouteGroup routes() {
        return null;    // Default implementation since static methods in interface require default implementation
    }
}
