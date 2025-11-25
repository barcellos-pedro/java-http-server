import java.io.IOException;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Router {
    public static void handle(Socket socket) {
        try (var response = socket.getOutputStream()) {
            var request = Request.of(socket);
            var data = route(request);
            IO.println(request);
            response.write(data.getBytes(UTF_8));
        } catch (IOException exception) {
            IO.println("[REQUEST:ERROR] " + exception);
        }
    }

    public static String route(Request request) throws IOException {
        return Routes.getOrNotFound(request);
    }
}
