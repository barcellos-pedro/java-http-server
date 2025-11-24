import module java.base;

public class HttpServer {
    private final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public static HttpServer create(int port) {
        return new HttpServer(port);
    }

    public void start() {
        IO.println("Server running at http://localhost:" + port);

        try (var server = new ServerSocket(port)) {
            for (; ; ) {
                try (var socket = server.accept()) {
                    var request = Request.of(socket);
                    IO.println(request);

                    try (var response = socket.getOutputStream()) {
                        var data = Controller.handle(request);
                        response.write(data);
                    }
                } catch (IOException exception) {
                    IO.println("[REQUEST:ERROR] " + exception);
                }
            }
        } catch (IOException exception) {
            IO.println("[SERVER:ERROR] " + exception);
        }
    }

}
