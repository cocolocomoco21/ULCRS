package ulcrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;
import ulcrs.controllers.CourseController;
import ulcrs.controllers.ScheduleController;
import ulcrs.controllers.SessionController;
import ulcrs.controllers.ShiftController;
import ulcrs.controllers.TutorController;

public class Server {

    private static Logger log = LoggerFactory.getLogger(Server.class);
    private TutorController tutorController;
    private CourseController courseController;
    private ScheduleController scheduleController;
    private ShiftController shiftController;
    private SessionController sessionController;

    public static void main(String args[]) {
        Server server = startServer();

        // Route to the proper methods
        Spark.path("/ulcrs", () -> {
            // Paths for resources, handled by appropriate Controllers
            Spark.path("/tutor", server.tutorController.routes());
            Spark.path("/course", server.courseController.routes());
            Spark.path("/schedule", server.scheduleController.routes());
            Spark.path("/shift", server.shiftController.routes());
            Spark.path("/session", server.sessionController.routes());
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
        Spark.staticFiles.location("/");

        server.initializeControllers();
        Spark.init();

        return server;
    }

    /**
     * Initialize all controllers for the server.
     */
    private void initializeControllers() {
        this.tutorController = new TutorController();
        this.courseController = new CourseController();
        this.scheduleController = new ScheduleController();
        this.shiftController = new ShiftController();
        this.sessionController = new SessionController();
    }

}