import java.io.IOException;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Controller {
    public static void handle(Socket socket) {
        try (var response = socket.getOutputStream()) {
            var data = Controller.route(socket);
            response.write(data.getBytes(UTF_8));
        } catch (IOException exception) {
            IO.println("[REQUEST:ERROR] " + exception);
        }
    }

    public static String route(Socket socket) throws IOException {
        var request = Request.of(socket);
        IO.println(request);

        return switch (request.getPath()) {
            case "/greeting" -> handleGretting();
            case "/message" -> handleMessage(request);
            default -> handleNotFound(request);
        };
    }

    public static String handleGretting() {
        return Response.of("Hello World");
    }

    public static String handleMessage(Request request) throws IOException {
        if (!request.getMethod().equals(HttpMethod.POST)) {
            return Response.of("""
                    { "message": "Request method must be a POST." }""", Response.Type.JSON);
        }

        if (!request.hasBody()) {
            return Response.of("""
                    { "message": "Missing body in request." }""", Response.Type.JSON);
        }

        return Response.echo(request, Response.Type.JSON);
    }

    public static String handleNotFound(Request request) {
        return Response.of("No static resource found for " + request.getPath() + ".");
    }
}
