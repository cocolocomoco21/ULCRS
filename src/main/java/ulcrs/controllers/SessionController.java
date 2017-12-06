package ulcrs.controllers;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import spark.Request;
import spark.Response;
import spark.RouteGroup;
import ulcrs.models.schedule.Schedule;
import ulcrs.models.session.Session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

    private static final String WORKSPACE_PATH = "sessions/";

    @Override
    public RouteGroup routes() {
        return () -> {
            before("/*", (request, response) -> log.info("endpoint: " + request.pathInfo()));
            get("/", this::getSessionList, exposeOnlyGson::toJson);
            get("/:name", this::getSession, gson::toJson);
            post("/", this::saveSession, gson::toJson);
            post("/:name", this::saveSession, gson::toJson);
            delete("/:name", this::deleteSession, gson::toJson);
        };
    }

    private List<String> getSessionList(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        String limitStr = request.queryParams("limit");
        int limit = Integer.MAX_VALUE;
        if (limitStr != null) {
            limit = Integer.valueOf(limitStr);
        }

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

    private Session getSession(Request request, Response response) throws IOException {
        response.type(CONTENT_TYPE_JSON);
        String name = addJsonExtension(request.params(":name"));

        try {
            FileReader fileReader = new FileReader(WORKSPACE_PATH + name);
            JsonReader reader = new JsonReader(fileReader);
            return gson.fromJson(reader, new TypeToken<Session>() {
            }.getType());
        } catch (FileNotFoundException e) {
            log.warn("File " + WORKSPACE_PATH + name + " not found.");
            return null;
        }
    }

    // Take a schedule, save to new session
    private boolean saveSession(Request request, Response response) {
        // TODO: Use real schedule
        InputStream is = SessionController.class.getClassLoader().getResourceAsStream("mockSchedule_FrontendExpects.json");
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        List<Schedule> schedules = gson.fromJson(reader, new TypeToken<List<Schedule>>() {
        }.getType());

        String filename = addJsonExtension(request.params(":name"));

        Session session;
        if (request.params(":name") == null) {
            session = new Session();
        } else {
            session = new Session(filename);
        }

        if (schedules.get(0) != null) {
            session.setExistingSchedule(schedules.get(0));
        }

        PrintWriter printWriter = null;
        try {
            // Make "sessions" directory if it doesn't exist
            File sessionsDir = new File(WORKSPACE_PATH);
            sessionsDir.mkdir();

            printWriter = new PrintWriter(WORKSPACE_PATH + session.getName());
            printWriter.println(gson.toJson(session));
        } catch (IOException e) {
            log.warn(e.getMessage());
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
        String name = addJsonExtension(request.params(":name"));

        File file = new File(WORKSPACE_PATH + name);
        return file.exists() && file.isFile() && file.delete();
    }

    private String addJsonExtension(String name) {
        // TODO make this make sense, added here for Iteration 2 deadline
        if (name == null) {
            return "";
        }

        if (!name.endsWith(".json")) {
            if (!name.endsWith(".")) {
                name = name.concat(".");
            }
            name = name.concat("json");
        }
        return name;
    }
}
