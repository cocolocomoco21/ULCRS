import static spark.Spark.get;
import com.google.gson.Gson;

public class Server {

    public static void main(String args[]) {
        System.out.println("Hello world");

        Gson gson = new Gson();
        get("/hello", (request, response) -> "Hello world", gson::toJson);
    }
    
}