package ulcrs.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class DataFetch {

    // Constants for request
    private static final String DROP_IN_REQUEST_URL = "https://dropin.engr.wisc.edu/services/DIMainController.php";
    private static final String DROP_IN_REQUEST_DATA = "service=ScheduleService&function=exportSchedulerInformation";
    private static final String DROP_IN_REQUEST_REQUEST_TYPE = "POST";
    private static final String DROP_IN_REQUEST_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String DROP_IN_REQUEST_CONTENT_LANGUAGE = "en-US";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String COOKIE = "Cookie";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CONTENT_LANGUAGE = "Content-Language";

    // Constants for request to direct JSON file
    private static final String DROP_IN_JSON_URL = "https://dropin.engr.wisc.edu/services/ulcrs-requirements.json";
    private static final String DROP_IN_JSON_REQUEST_TYPE = "GET";

    private static Logger log = LoggerFactory.getLogger(DataFetch.class);

    /**
     * Fetch data from the ULC server and return response in form of List<String>.
     * <p>
     * Specifically, this hits the Drop-In endpoint to retrieve course, shift, tutor, and tutor preference data.
     * However, Drop-In a peculiar way of doing requests, so we issue an HTTP POST as follows:
     * - URL: https://dropin-dev.engr.wisc.edu/services/DIMainController.php
     * - Set Content-Type to application/x-www-form-urlencoded
     * - Set request data to: service=ScheduleService&function=exportSchedulerInformation
     * - You must include the shibboleth cookie/credentials (passed as String)
     * - You must have admin credentials on the dev website (I believe Matt and Jason have this).
     *
     * @param cookie String - shibboleth cookie used for authenticating with the UW NetID Service, to validate the call.
     * @return List<String> - the response from the ULC server call. Should contain one line of JSON. In case of
     * failure, returns empty List.
     */
    static List<String> fetchFromULCServer(String cookie) {
        if (cookie == null || cookie.equals("")) {
            // No cookie is passed in, fail silently
            return new ArrayList<>();
        }

        HttpURLConnection connection = null;

        try {
            // Note:
            // The below commented code is the code required to hit the ULC endpoint to get ULC data.
            //
            // For now, we are accessing a json file on the ULC server directly to get this ULC data since this file
            // has the necessary course requirement information, while the endpoint does not yet.

            /*
            // Initialize HTTP request
            URL url = new URL(DROP_IN_REQUEST_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(DROP_IN_REQUEST_REQUEST_TYPE);
            connection.setRequestProperty(CONTENT_TYPE, DROP_IN_REQUEST_CONTENT_TYPE);
            connection.setRequestProperty(COOKIE, cookie);
            connection.setRequestProperty(CONTENT_LENGTH, Integer.toString(DROP_IN_REQUEST_DATA.getBytes().length));
            connection.setRequestProperty(CONTENT_LANGUAGE, DROP_IN_REQUEST_CONTENT_LANGUAGE);
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(DROP_IN_REQUEST_DATA);
            wr.close();
            */

            // Initialize HTTP request
            URL url = new URL(DROP_IN_JSON_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(DROP_IN_JSON_REQUEST_TYPE);
            connection.setRequestProperty(COOKIE, cookie);

            int responseCode = connection.getResponseCode();

            // Get response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            log.info("Response code: " + responseCode + " from ULC fetch");

            // Parse response
            List<String> response = new ArrayList<String>();
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                //response.add(line);   // Commented from ULC endpoint code - left as reference.
                builder.append(line.trim());
            }
            rd.close();
            response.add(builder.toString());

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
