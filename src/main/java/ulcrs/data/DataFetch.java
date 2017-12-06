package ulcrs.data;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class DataFetch {

    // Constants for request
    private final static String DROP_IN_REQUEST_URL = "https://dropin.engr.wisc.edu/services/DIMainController.php";
    private final static String DROP_IN_REQUEST_DATA = "service=ScheduleService&function=exportSchedulerInformation";
    private final static String DROP_IN_REQUEST_REQUEST_TYPE = "POST";
    private final static String DROP_IN_REQUEST_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private final static String DROP_IN_REQUEST_CONTENT_LANGUAGE = "en-US";
    private final static String CONTENT_TYPE = "Content-Type";
    private final static String COOKIE = "Cookie";
    private final static String CONTENT_LENGTH = "Content-Length";
    private final static String CONTENT_LANGUAGE = "Content-Language";

    /**
     * Fetch data from the ULC server and return response in form of List<String>.
     *
     * Specifically, this hits the Drop-In endpoint to retrieve course, shift, tutor, and tutor preference data.
     * However, Drop-In a peculiar way of doing requests, so we issue an HTTP POST as follows:
     *      - URL: https://dropin-dev.engr.wisc.edu/services/DIMainController.php
     *      - Set Content-Type to application/x-www-form-urlencoded
     *      - Set request data to: service=ScheduleService&function=exportSchedulerInformation
     *      - You must include the shibboleth cookie/credentials (passed as String)
     *      - You must have admin credentials on the dev website (I believe Matt and Jason have this).
     *
     * @param cookie String - shibboleth cookie used for authenticating with the UW NetID Service, to validate the call.
     * @return List<String> - the response from the ULC server call. Should contain one line of JSON. In case of
     *          failure, returns empty List.
     */
    static List<String> fetchFromULCServer(String cookie) {
        if (cookie == null || cookie.equals("")) {
            // No cookie is passed in, fail silently
            return new ArrayList<>();
        }

        HttpURLConnection connection = null;

        try
        {
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

            // Get response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            // Parse response
            List<String> response = new ArrayList<String>();
            String line;
            while ((line = rd.readLine()) != null)
            {
                response.add(line);
            }
            rd.close();

            return response;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if (connection != null)
                connection.disconnect();
        }
    }
}
