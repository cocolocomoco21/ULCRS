package ulcrs;

import static spark.Spark.before;
import static spark.Spark.init;
import static spark.Spark.path;
import static spark.Spark.staticFiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulcrs.controllers.CourseController;
import ulcrs.controllers.ScheduleController;
import ulcrs.controllers.TutorController;

public class Server {

    private static Logger log = LoggerFactory.getLogger(Server.class);
    private TutorController tutorController;
    private CourseController courseController;
    private ScheduleController scheduleController;


    public static void main(String args[]) {
        Server server = startServer();

        // Route do the proper methods
        path("/ulcrs", () -> {
            before("/*", (q, a) -> log.info("Received api call"));
            path("/tutor", server.tutorController.routes());
            path("/course", server.courseController.routes());
            path("/schedule", server.scheduleController.routes());
        });
    }

    /**
     * Create and start the Server object used to route API calls. The Server object simply acts as a container for
     * API routing facilitated by Sparkjava and controllers used in this routing.
     *
     * @return Server - the Server object to act as the server
     */
    private static Server startServer() {
        Server server = new Server();
        staticFiles.location("/");

        server.initializeControllers();
        init();

        return server;
    }

    /**
     * Initialize all controllers for the server.
     */
    private void initializeControllers() {
        this.tutorController = new TutorController();
        this.courseController = new CourseController();
        this.scheduleController = new ScheduleController();
    }

}