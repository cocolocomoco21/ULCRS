package ulcrs;

import static spark.Spark.before;
import static spark.Spark.path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ulcrs.controllers.TutorController;

public class Server {

    private static Logger log = LoggerFactory.getLogger(Server.class);

    public static void main(String args[]) {
        // Route do the proper methods
        path("/ulcrs", () -> {
            before("/*", (q, a) -> log.info("Received api call"));
            path("/tutor", TutorController.routes());
        });
    }

}