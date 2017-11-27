package ulcrs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonFactory {

    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        }
        return gson;
    }
}
