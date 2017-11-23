package ulcrs.controllers;

import spark.Request;
import spark.Response;
import spark.RouteGroup;
import ulcrs.models.course.Course;
import ulcrs.models.course.CourseRequirements;
import ulcrs.models.schedule.Schedule;
import ulcrs.models.session.Session;
import ulcrs.models.shift.ScheduledShift;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

public class SessionController extends BaseController {

    @Override
    public RouteGroup routes() {
        return () -> {
            before("/*", (request, response) -> log.info("endpoint: " + request.pathInfo()));
            get("/", this::getSessionList, gson::toJson);
            get("/:name", this::getSession, gson::toJson);
            post("/:name", this::updateSession, gson::toJson);
            delete("/:name", this::deleteSession, gson::toJson);
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
    private boolean updateSession(Request request, Response response) throws IOException {
        // TODO: update session matching name

        Session session = new Session();

        Schedule schedule = new Schedule();
        session.setExistingSchedule(schedule);

        Set<ScheduledShift> scheduledShifts = new HashSet<>();
        schedule.setScheduledShifts(scheduledShifts);

//        scheduledShifts.add(new ScheduledShift());
//
//        Course course = new Course("1", "course 1", new CourseRequirements());
        return false;
    }

    // TODO: What return type?
    private boolean deleteSession(Request request, Response response) {
        // TODO: delete session matching name
        return false;
    }
}
