package ulcrs;

import com.google.gson.Gson;

public class GsonFactory {

    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }
}
