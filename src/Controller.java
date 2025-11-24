import java.io.IOException;

public class Controller {
    public static byte[] handle(Request request) throws IOException {
        return switch (request.getPath()) {
            case "/greeting" -> handleGretting();
            case "/message" -> handleMessage(request);
            default -> handleNotFound(request);
        };
    }

    public static byte[] handleGretting() {
        return Response.of("Hello World");
    }

    public static byte[] handleMessage(Request request) throws IOException {
        if (request.hasBody()) {
            return Response.echo(request, Response.Type.JSON);
        }

        return Response.of("""
                { "message": "Missing body in request." }""", Response.Type.JSON);
    }

    public static byte[] handleNotFound(Request request) {
        return Response.of("No static resource found for " + request.getPath() + ".");
    }
}
