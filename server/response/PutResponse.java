package server.response;

import server.request.Request;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Handles HTTP PUT requests.
 */
public class PutResponse {
    /**
     * Creates a response for an HTTP PUT request.
     *
     * @param request  the request
     * @param path     the path of the request
     * @param documentRoot the root directory of the server
     * @return a response
     */
    public static Response createPutResponse(Request request, String path, String documentRoot) {

        // Check if the request is authenticated
        Authenticator authenticator = new Authenticator();
        Response authResponse = authenticator.authenticate(request, path, documentRoot);
        if (authResponse != null) {
            return authResponse;
        }

        // Create the file if it does not exist
        File file = new File(path);
        if (!file.exists()) {
            // Create parent directories if they do not exist
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    System.err.println("Error creating directory: " + file.getParent());
                    return ErrorResponse
                            .createErrorResponse(500, "Internal Server Error: Cannot create directory");
                }
            }

            // Create the file
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating file: " + e.getMessage());
                return ErrorResponse
                        .createErrorResponse(500, "Internal Server Error: Cannot create file");
            }
        }

        // Write the request body to the file
        try {
            byte[] data = request.getBody();
            Files.write(file.toPath(), data);
            Response response = new Response();
            response.setStatusCode(201); // Created
            return response;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            return ErrorResponse
                    .createErrorResponse(500, "Internal Server Error: Cannot write to file");
        }
    }
}
