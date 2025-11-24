import module java.base;

public class Request {
    private String method;
    private String path;
    private String protocol;
    private Map<String, String> headers;
    private BufferedReader reader;

    public Request() {
    }

    public static Request of(Socket socket) throws IOException {
        var request = new Request();
        var reader = getReader(socket);
        request.setReader(reader);
        request.setRequestLineFields();
        request.parseHeaders();
        return request;
    }

    /// - InputStream -> byte[]
    /// - InputStreamReader -> char[]
    /// - BufferedReader -> string for each line
    public static BufferedReader getReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void parseHeaders() throws IOException {
        var data = "";
        var headers = new HashMap<String, String>();

        while (!(data = reader.readLine()).isEmpty()) {
            var keyValue = data.split(": ", 2);

            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                headers.put(key, value);
            }
        }

        setHeaders(headers);
    }

    public char[] parseBody() throws IOException {
        var contentLength = Integer.parseInt(headers.get("Content-Length"));
        var body = new char[contentLength];
        int bytesRead = reader.read(body, 0, contentLength);
        IO.println("Body bytes parsed: " + bytesRead);
        return body;
    }

    public boolean hasBody() {
        return headers.containsKey("Content-Length");
    }

    private String[] getRequestLine() throws IOException {
        return getReader().readLine().split(" ", 3);
    }

    public void setRequestLineFields() throws IOException {
        var requestLine = getRequestLine();
        setMethod(requestLine[0]);
        setPath(requestLine[1]);
        setProtocol(requestLine[2]);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", protocol='" + protocol + '\'' +
                ", headers=" + headers +
                ", reader=" + reader +
                '}';
    }
}