package ulcrs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonFactory {

    private static Gson gson;
    private static Gson exposeOnlyGson;

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        }
        return gson;
    }

    public static Gson getExposeOnlyGson() {
        if (exposeOnlyGson == null) {
            exposeOnlyGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        }
        return exposeOnlyGson;
    }
}
