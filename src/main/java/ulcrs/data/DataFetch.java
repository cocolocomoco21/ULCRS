package ulcrs.data;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataFetch {

    private static String DROP_IN_REQUEST_URL = "https://dropin-dev.engr.wisc.edu/services/DIMainController.php";
    private static String DROP_IN_REQUEST_DATA = "service=ScheduleService&function=exportSchedulerInformation";
    private static String DROP_IN_REQUEST_REQUEST_TYPE = "POST";
    private static String DROP_IN_REQUEST_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static String DROP_IN_REQUEST_CONTENT_LANGUAGE = "en-US";


    /*
    William Jen [12:40 AM]
    I have implemented the Drop-In endpoint to retrieve course, shift, tutor, and tutor preference data. However, Drop-In has a peculiar way of doing requests. You must issue a HTTP POST to the following:
    •    URL: https://dropin-dev.engr.wisc.edu/services/DIMainController.php
    •    Set Content-Type to application/x-www-form-urlencoded
    •    Set request data to: service=ScheduleService&function=exportSchedulerInformation
    •    You must include the shibboleth cookie/credentials.
    •    You must have admin credentials on the dev website (I believe Matt and Jason have this).
     */

    public static List<String> fetchFromULCServer(String cookie) {
        HttpURLConnection connection = null;

        try
        {
            URL url = new URL(DROP_IN_REQUEST_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(DROP_IN_REQUEST_REQUEST_TYPE);
            connection.setRequestProperty("Content-Type", DROP_IN_REQUEST_CONTENT_TYPE);
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("Content-Length", Integer.toString(DROP_IN_REQUEST_DATA.getBytes().length));
            connection.setRequestProperty("Content-Language", DROP_IN_REQUEST_CONTENT_LANGUAGE);


            connection.setUseCaches(false);
            connection.setDoOutput(true);

            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(DROP_IN_REQUEST_DATA);
            wr.close();

            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

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

    public static void main(String[] args) throws Exception
    {
        List<String> response = fetchFromULCServer("");
        Files.write(Paths.get("E:\\Users\\Matt\\UW-Madison\\Courses\\2017-2018\\Fall 2017\\CS 506\\Project\\Code\\Testing\\test.json"), response, Charset.forName("UTF-8"));
    }
}
