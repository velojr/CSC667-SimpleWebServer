package server.response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import server.request.Request;
import server.tools.MimeTypeUtils;

public class HeadResponse {
    /**
     * Creates a HEAD response based on the given request and path.
     * 
     * @param request The HTTP request
     * @param path    The path of the request
     * @param documentRoot The root directory of the server
     * @return The response to the request
     */
    public static Response createHeadResponse(Request request, String path, String documentRoot) {

        // Check if the file is protected by a .password file
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
            response.setContentLength(fileContent.length);
            response.setContentType(MimeTypeUtils.getMimeTypeFromExtension(path));
            return response;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return ErrorResponse
                    .createErrorResponse(500, "Internal Server Error: Cannot read file");
        }
    }
}