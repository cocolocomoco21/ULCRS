package ulcrs.controllers;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import spark.Request;
import spark.Response;
import spark.RouteGroup;
import ulcrs.models.schedule.Schedule;
import ulcrs.models.session.Session;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

public class SessionController extends BaseController {

    private static final String WORKSPACE_PATH = "workspace/";

    @Override
    public RouteGroup routes() {
        return () -> {
            before("/*", (request, response) -> log.info("endpoint: " + request.pathInfo()));
            get("/", this::getSessionList, exposeOnlyGson::toJson);
            get("/:name", this::getSession, gson::toJson);
            post("/:name", this::saveSession, gson::toJson);
            delete("/:name", this::deleteSession, gson::toJson);
        };
    }

    private List<String> getSessionList(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);
        int limit = Integer.valueOf(request.queryParams("limit"));

        List<String> filenames = new ArrayList<>();
        File folder = new File(WORKSPACE_PATH);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            Arrays.sort(listOfFiles, Collections.reverseOrder());
            for (int i = 0; i < limit && i < listOfFiles.length; i++) {
                File file = listOfFiles[i];
                if (file.isFile()) {
                    filenames.add(file.getName());
                }
            }
        }
        return filenames;
    }

    private Session getSession(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);
        String name = request.params("name");

        InputStream is = SessionController.class.getClassLoader().getResourceAsStream(WORKSPACE_PATH + name);
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        return gson.fromJson(reader, new TypeToken<Session>() {
        }.getType());
    }

    // Take a schedule, save to new session
    private boolean saveSession(Request request, Response response) {
        // TODO: Use real schedule
        InputStream is = SessionController.class.getClassLoader().getResourceAsStream("mockSchedule_FrontendExpects.json");
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        List<Schedule> schedules = gson.fromJson(reader, new TypeToken<List<Schedule>>() {
        }.getType());

        String filename = request.params("name");

        Session session;
        if (filename == null) {
            session = new Session();
        } else {
            session = new Session(filename);
        }

        if (schedules.get(0) != null) {
            session.setExistingSchedule(schedules.get(0));
        }

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(session.getName());
            printWriter.println(gson.toJson(this));
        } catch (IOException e) {
            return false;
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
        return true;
    }

    private boolean deleteSession(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);
        String name = request.params("name");

        File file = new File(WORKSPACE_PATH + name);
        return file.exists() && file.isFile() && file.delete();
    }
}
