package ulcrs.controllers;

import spark.Request;
import spark.Response;
import spark.RouteGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static spark.Spark.before;
import static spark.Spark.get;

public class UlcController extends BaseController {

    private static final String TEST_ENDPOINT = "https://tbrtest.engr.wisc.edu/";

    @Override
    public RouteGroup routes() {
        return () -> {
            before("/*", (request, response) -> log.info("endpoint: " + request.pathInfo()));
            get("/login", this::loginTest);
        };
    }

    private String loginTest(Request request, Response response) {

        URL url;
        URLConnection urlConn;
        BufferedReader br = null;
        String line;

        StringBuilder page = new StringBuilder();

        try {
            url = new URL(TEST_ENDPOINT);
            urlConn = url.openConnection();

            // insert cookie here
            urlConn.setRequestProperty("Cookie", "<cookie name>=<cookie>");

            urlConn.connect();
            urlConn.getContent();

            br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            while ((line = br.readLine()) != null) {
                page.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ioe) {
                // nothing to see here
            }
        }

        return page.toString();
    }
}
