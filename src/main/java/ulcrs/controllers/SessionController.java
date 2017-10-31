package ulcrs.controllers;

import spark.Request;
import spark.Response;
import spark.RouteGroup;
import spark.Spark;
import ulcrs.models.session.Session;

import java.util.List;

public class SessionController extends BaseController {

    @Override
    public RouteGroup routes() {
        return () -> {
            Spark.before("/*", (request, response) -> log.info("endpoint: " + request.pathInfo()));
            Spark.get("", this::getSessionList, gson::toJson);
            Spark.get("/:name", this::getSession, gson::toJson);
            Spark.post("/:name", this::updateSession, gson::toJson);
            Spark.delete("/:name", this::deleteSession, gson::toJson);
        };
    }

    private List<String> getSessionList(Request request, Response response) {
        // TODO: return names of sessions with limit specified
        return null;
    }

    private Session getSession(Request request, Response response) {
        // TODO: return session matching name
        return null;
    }

    // TODO: What return type?
    private Object updateSession(Request request, Response response) {
        // TODO: update session matching name
        return false;
    }

    // TODO: What return type?
    private Object deleteSession(Request request, Response response) {
        // TODO: delete session matching name
        return false;
    }
}
