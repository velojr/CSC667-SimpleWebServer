package server.response;

import server.request.Request;

/**
 * A factory class to create responses based on the HTTP method of the request.
 */
public class ResponseFactory {
    /**
     * Create a response based on the HTTP method of the request.
     * 
     * @param documentRoot the document root of the web server
     * @param request      the request to create the response for
     * @return the response created based on the request method
     */
    public static Response createResponse(String documentRoot, Request request) {
        String method = request.getMethod();
        String path = documentRoot + request.getPath();

        // Default to index.html if path ends with a directory slash
        if (path.endsWith("/")) {
            path += "index.html";
        }

        // Switch based on the method to create the appropriate response
        return switch (method.toUpperCase()) {
            // Create a GET response
            case "GET" -> GetResponse.createGetResponse(request, path, documentRoot);
            // Create a HEAD response
            case "HEAD" -> HeadResponse.createHeadResponse(request, path, documentRoot);
            // Create a PUT response
            case "PUT" -> PutResponse.createPutResponse(request, path, documentRoot);
            // Create a DELETE response
            case "DELETE" -> DeleteResponse.createDeleteResponse(request, path, documentRoot);
            // Default to a 405 error if the method is not supported
            default -> ErrorResponse.createErrorResponse(405, "Method Not Allowed");
        };
    }
}