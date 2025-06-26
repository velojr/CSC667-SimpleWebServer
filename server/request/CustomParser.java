package server.request;

import java.util.Arrays;

/**
 * Custom parser for HTTP requests.
 *
 * HTTP requests have the following general format:
 *
 * <Request-Line>
 * <headers>
 * <empty line>
 * <body>
 *
 * <Request-Line> is "Method Path HTTP-Version" (e.g. "GET / HTTP/1.1")
 * <headers> are key-value pairs separated by colon and space (e.g. "Host: localhost:8080")
 * <empty line> is a line with no characters
 * <body> is the data sent with the request (e.g. a file upload)
 */
public class CustomParser {
    public static ParsedRequest parse(byte[] requestBytes) {

        ParsedRequest parsedRequest = new ParsedRequest();
        String request = new String(requestBytes);
        System.out.println("Parsing request: " + request);

        String[] lines = request.split("(\r\n|\r|\n)");
        if (lines.length == 0) {
            throw new IllegalArgumentException("Empty request");
        }

        // Parse request line (Method, Path, HTTP Version)
        String[] methodParts = lines[0].split(" ");
        if (methodParts.length < 3) {
            throw new IllegalArgumentException("Invalid request line");
        }
        parsedRequest.setMethod(methodParts[0]);
        String path = methodParts[1];

        // Extract query parameters (if any)
        if (path.contains("?")) {
            String[] pathAndQuery = path.split("\\?", 2);
            parsedRequest.setPath(pathAndQuery[0]);

            String[] queryParams = pathAndQuery[1].split("&");
            for (String queryParam : queryParams) {
                String[] keyValue = queryParam.split("=", 2);
                if (keyValue.length == 2) {
                    parsedRequest.setQueryParam(keyValue[0], keyValue[1]);
                }
            }
        } else {
            parsedRequest.setPath(path);
        }

        // Parse headers
        int i = 1;
        for (; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) break; // End of headers

            String[] headerParts = line.split(":", 2);
            if (headerParts.length == 2) {
                parsedRequest.setHeaderValue(headerParts[0].trim(), headerParts[1].trim());
            }
        }

        // Parse body
        String contentLength = parsedRequest.getHeaderValue("Content-Length");
        if (contentLength != null) {
            try {
                int contentLengthValue = Integer.parseInt(contentLength);
                int bodyStartIndex = requestBytes.length - contentLengthValue;
                byte[] bodyBytes = Arrays.copyOfRange
                        (requestBytes, bodyStartIndex, requestBytes.length);
                parsedRequest.setBody(bodyBytes);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid Content-Length header value", e);
            }
        }

        return parsedRequest;
    }
}
