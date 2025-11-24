import module java.base;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Response {
    public static String of(String value) {
        return of(value, Type.TEXT);
    }

    public static String of(String value, Type type) {
        return bindValues(type, value);
    }

    public static String of(char[] value, Type type) {
        return bindValues(type, new String(value));
    }

    public static String echo(Request request, Type type) throws IOException {
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
