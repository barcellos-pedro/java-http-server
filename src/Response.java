import module java.base;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Response {
    public static byte[] of(String value) {
        return of(value, Type.TEXT);
    }

    public static byte[] of(String value, Type type) {
        var responseBody = bindValues(type, value);
        return responseBody.getBytes(UTF_8);
    }

    public static byte[] of(char[] value, Type type) {
        var responseBody = bindValues(type, new String(value));
        return responseBody.getBytes(UTF_8);
    }

    public static byte[] echo(Request request, Type type) throws IOException {
        var reqBody = request.parseBody();
        return of(reqBody, type);
    }

    public static String bindValues(Type type, String data) {
        var contentLength = data.getBytes(UTF_8).length;

        return switch (type) {
            case TEXT -> ResponseTemplate.TEXT.formatted(contentLength, data);
            case JSON -> ResponseTemplate.JSON.formatted(contentLength, data);
        };
    }

    public enum Type {
        TEXT, JSON
    }
}
