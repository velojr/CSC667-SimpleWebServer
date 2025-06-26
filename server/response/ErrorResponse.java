package server.response;

/**
 * Utility class for creating error responses.
 */
public class ErrorResponse {
    /**
     * Create an error response.
     * 
     * @param statusCode The status code of the response
     * @param message    The error message
     * @return The response to send back to the client
     */
    public static Response createErrorResponse(int statusCode, String message) {
        Response response = new Response();
        response.setStatusCode(statusCode);
        response.setContent(("Error " + statusCode + ": " + message).getBytes());
        response.setContentLength(("Error " + statusCode + ": " + message).getBytes().length);
        response.setContentType("text/plain");
        return response;
    }
}