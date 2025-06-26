package server.response;

import server.request.Request;

import java.io.File;
import java.io.IOException;

/**
 * DeleteResponse is a special case of Response that handles the DELETE HTTP method.
 * It will delete the requested file and return a success response.
 */
public class DeleteResponse {

    public static Response createDeleteResponse(Request request, String path, String documentRoot) {

        // Check if the file is protected by a password
        Authenticator authenticator = new Authenticator();
        Response authResponse = authenticator.authenticate(request, path, documentRoot);
        if (authResponse != null) {
            return authResponse;
        }

        File file = new File(path);

        // Check if the file exists
        if (!file.exists()) {
            return ErrorResponse.createErrorResponse(404, "File Not Found: " + path);
        }

        // Delete the file and check for success
        if (!file.delete()) {
            System.err.println("Error deleting file: " + path);
            return ErrorResponse
                    .createErrorResponse(500, "Internal Server Error: Cannot delete file");
        }

        Response response = new Response();

        // Set the status code to 204 (No Content)
        response.setStatusCode(204);

        // Set the content to a success message
        response.setContent("File deleted successfully".getBytes());

        return response;
    }
}
