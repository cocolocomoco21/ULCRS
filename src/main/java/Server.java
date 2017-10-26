import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;
import controllers.TutorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.RouteGroup;

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