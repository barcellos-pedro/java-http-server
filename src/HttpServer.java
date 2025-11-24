import module java.base;

public class HttpServer {
    private final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public static HttpServer create(int port) {
        return new HttpServer(port);
    }

    void listen(Consumer<Socket> handler) throws IOException {
        IO.println("Server running at http://localhost:" + port);

        try (var server = new ServerSocket(port)) {
            for (; ; ) {
                try (var socket = server.accept()) {
                    handler.accept(socket);
                } catch (IOException exception) {
                    IO.println("[REQUEST:ERROR] " + exception);
                }
            }
        }
    }
}
