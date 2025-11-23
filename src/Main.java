import static java.nio.charset.StandardCharsets.UTF_8;

void main() throws IOException {
    // Create Server
    var server = new ServerSocket(8080);

    // Main Loop
    for (; ; ) {
        // Accept client connection/socket
        try (var socket = server.accept()) {
            var reader = getRequestReader(socket);

            var data = reader.readLine();
            IO.println("[" + data + "]"); // request line

            var headers = new HashMap<String, String>();

            while (!(data = reader.readLine()).isEmpty()) {
                var keyValue = data.split(": ", 2);

                if (keyValue.length == 2) {
                    headers.put(keyValue[0], keyValue[1]);
                }
            }

            IO.println(headers);

            try (var out = socket.getOutputStream()) {
                var responseData = containsBody(headers) ? "pong" : "hello";
                byte[] responseBody = buildResponse(responseData);
                out.write(responseBody);
            }
        } catch (IOException exception) {
            IO.println("[ERROR] " + exception);
        }
    }
}

private static boolean containsBody(HashMap<String, String> headers) {
    return headers.containsKey("Content-Length");
}

public static String TEMPLATE = """
        HTTP/1.1 200 OK\r
        Content-Length: %s\r
        Content-Type: text/plain\r
        \r
        %s""";

private static byte[] buildResponse(String value) {
    byte[] bodyBytes = value.getBytes(UTF_8);
    var response = TEMPLATE.formatted(bodyBytes.length, value);
    return response.getBytes(UTF_8);
}

/// - InputStream -> byte[]
/// - InputStreamReader -> char[]
/// - BufferedReader -> string for each line
private static BufferedReader getRequestReader(Socket socket) throws IOException {
    return new BufferedReader(new InputStreamReader(socket.getInputStream()));
}
