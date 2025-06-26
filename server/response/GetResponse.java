package server.response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import server.request.Request;
import server.tools.MimeTypeUtils;

/**
 * Class that handles GET requests.
 */
public class GetResponse {
    /**
     * Creates a GET response based on the given request and document root.
     *
     * @param request  The request to handle.
     * @param path     The path from the request.
     * @param documentRoot The document root of the server.
     * @return The response to send back to the client, or null if there was an error.
     */
    public static Response createGetResponse(Request request,String path, String documentRoot) {

        // Check for authentication
        Authenticator authenticator = new Authenticator();
        Response authResponse = authenticator.authenticate(request, path, documentRoot);
        if (authResponse != null) {
            return authResponse;
        }

        // Check if the file exists and is a file
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            return ErrorResponse.createErrorResponse(404, "File Not Found: " + path);
        }

        // Read the file content and set the content type and length
        try {
            System.out.println("file path: " + file.toPath());
            byte[] fileContent = Files.readAllBytes(file.toPath());
            Response response = new Response();
            response.setContent(fileContent);
            response.setContentLength(fileContent.length);
            response.setContentType(MimeTypeUtils.getMimeTypeFromExtension(path));
            return response;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            // Return an error response with a 500 Internal Server Error
            // status code and a message indicating that there was an
            // error reading the file.
            return ErrorResponse.createErrorResponse
                    (500, "Internal Server Error: Cannot read file");
        }
    }
}