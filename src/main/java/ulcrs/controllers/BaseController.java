package ulcrs.controllers;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.RouteGroup;

abstract class BaseController {

    static final String CONTENT_TYPE_JSON = "application/json";

    static Gson gson = new Gson();
    static Logger log = LoggerFactory.getLogger(BaseController.class);;

    abstract RouteGroup routes();
}
