package server.request;

import server.response.ErrorResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Reads from an input stream and parses the request based on the HTTP
 * specification.
 */
public class Request {

    private final InputStream inputStream;
    protected ParsedRequest parsedRequest;

    public Request(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Reads the request from the input stream and parses it. The parsed request
     * is stored in the parsedRequest field.
     */
    /**
     * Reads the request from the input stream and parses it. The parsed request
     * is stored in the parsedRequest field.
     */
    public void read() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            // Read available bytes from input stream
            while ((bytesRead = this.inputStream.read(buffer)) != -1) {
                if (bytesRead == 0) {
                    continue;
                }
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                System.out.println(bytesRead + " bytes read.");
                if (this.inputStream.available() == 0) {
                    break;
                }
            }

            byte[] input = byteArrayOutputStream.toByteArray();

            if (input.length > 0) {
                try {
                    // Parse the request according to the HTTP specification
                    this.parsedRequest = CustomParser.parse(input);
                } catch (IllegalArgumentException e) {
                    // If there is an error parsing the request, send an error
                    // response
                    System.err.println("Error parsing request: " + e.getMessage());
                    ErrorResponse.createErrorResponse(400, "Bad Request");
                }
            }

        } catch (IOException e) {
            // If there is an error reading the request, send an error response
            System.err.println("Error reading request: " + e.getMessage());
        }
    }

    /**
     * Gets the HTTP method of the request.
     *
     * @return the HTTP method
     */
    public String getMethod() {
        return (parsedRequest != null) ? parsedRequest.getMethod() : null;
    }

    /**
     * Gets the path of the request.
     *
     * @return the path
     */
    public String getPath() {
        return (parsedRequest != null) ? parsedRequest.getPath() : null;
    }

    /**
     * Gets the body of the request.
     *
     * @return the body
     */
    public byte[] getBody() {
        return (parsedRequest != null) ? parsedRequest.getBody() : null;
    }

    /**
     * Gets the value of the given header.
     *
     * @param key the header key
     * @return the value of the header
     */
    public String getHeader(String key) {
        return (parsedRequest != null) ? parsedRequest.getHeaderValue(key) : null;
    }

    /**
     * Gets the value of the given query parameter.
     *
     * @param key the query parameter key
     * @return the value of the query parameter
     */
    public String getQueryParam(String key) {
        return (parsedRequest != null) ? parsedRequest.getQueryParam(key) : null;
    }

    public String getHeaderValue(String key) {
        return (parsedRequest != null) ? parsedRequest.getHeaderValue(key) : null;
    }
}
