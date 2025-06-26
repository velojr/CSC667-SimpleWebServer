package server.response;

import server.tools.Time;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP response.
 * 
 * This class provides methods to set the HTTP status code, content type, content length,
 * and content of the response. It also provides a method to write the response to an output stream.
 */
public class Response {
    private int statusCode = 200; // default status code
    private int contentLength = 0; // default content length
    private String contentType = "text/plain"; // default content type
    private byte[] content = new byte[0]; // default content
    private Map<String, String> headers = new HashMap<>(); // map of headers

    /**
     * Set the HTTP status code for the response.
     * 
     * @param statusCode the status code
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Set the content type for the response.
     * 
     * @param contentType the content type
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Set the content length for the response.
     * 
     * @param contentLength the content length
     */
    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    /**
     * Set a header for the response.
     * 
     * @param key the key of the header
     * @param value the value of the header
     */
    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * Set the content as bytes (for binary data like images, files).
     * 
     * @param content the content
     */
    public void setContent(byte[] content) {
        this.content = content != null ? content : new byte[0];
    }

    /**
     * Set the content as a string (for text-based responses).
     * 
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content != null ? content.getBytes(StandardCharsets.UTF_8) : new byte[0];
    }

    /**
     * Write the response to the output stream.
     * 
     * @param outputStream the output stream
     * @throws IOException if an I/O error occurs
     */
    public void write(OutputStream outputStream) throws IOException {
        try {
            String responseHeaders = generateHeaders();

            outputStream.write(responseHeaders.getBytes(StandardCharsets.UTF_8));
            if (this.contentLength > 0 && this.content.length > 0) {
                outputStream.write(content);
            }

            outputStream.flush();
        } catch (IOException e) {
            System.err.println("Error writing response: " + e.getMessage());
        }
    }

    /**
     * Generate the HTTP headers for the response.
     * 
     * @return the HTTP headers
     */
    private String generateHeaders() {
        StringBuilder responseHeaders = new StringBuilder();
        responseHeaders.append("HTTP/1.1 ")
                .append(statusCode)
                .append(" ")
                .append(getStatusMessage()).append("\r\n");
        responseHeaders.append("Date: ").append(Time.getTime()).append("\r\n");

        if (isErrorStatusCode()) {
            responseHeaders.append("Connection: close\r\n\r\n");
        } else {
            responseHeaders.append("Content-Type: ").append(contentType).append("\r\n");
            responseHeaders.append("Content-Length: ").append(contentLength).append("\r\n");

            // Add headers from the headers map
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                responseHeaders.append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue()).append("\r\n");
            }

            responseHeaders.append("Connection: close\r\n\r\n");
        }

        return responseHeaders.toString();
    }

    /**
     * Check if the status code is an error status code.
     * 
     * @return true if it is an error status code, false otherwise
     */
    private boolean isErrorStatusCode() {
        return statusCode == 400 ||
                statusCode == 403 || 
                statusCode == 404 || 
                statusCode == 405 ||
                statusCode == 500;
    }

    /**
     * Get the status message for the given status code.
     * 
     * @return the status message
     */
    private String getStatusMessage() {
        return switch (statusCode) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 204 -> "No Content";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 500 -> "Internal Server Error";
            default -> "Unknown Status";
        };
    }
}