package server.response;

import server.request.Request;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Checks for a .password file in the current directory and all parent directories.
 * If such a file is found, it checks the username and password against the
 * contents of the file. If the username and password are found in the file, it
 * returns null to indicate success. If the username and password are not found
 * in the file, it responds with 403 Forbidden.
 *
 * If no .password file exists, it returns null to indicate success.
 */
public class Authenticator {
    public Response authenticate(Request request, String path, String documentRoot) {

        File file = new File(path);
        File parentDir = file.getParentFile();

        while (parentDir != null) {
            // Stop at the document root
            if (parentDir.getAbsolutePath().equals(documentRoot)) break;

            // Look for a .password file in the parent directory
            File passwordFile = new File(parentDir, ".passwords");
            Response response = authResponse(request, passwordFile);
            if (response != null) return response;

            // Move to the parent directory
            parentDir = parentDir.getParentFile();
        }

        // If no .password file exists, return null to indicate success
        return null;
    }

    private static Response authResponse(Request request, File passwordFile) {
        if (passwordFile.exists()) {
            // If no Authorization header is present, respond with 401 Unauthorized
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                Response response = new Response();
                response.setStatusCode(401);
                response.setHeader("WWW-Authenticate", "Basic realm=\"667 Server\"");
                return response;
            }

            // If an Authorization header is present, check the username and password
            String encoded = authHeader.replace("Basic ", "").trim();
            String decoded = new String(Base64.getDecoder().decode(encoded));
            System.out.println("Decoded: " + decoded);

            try {
                if (!approved(passwordFile, decoded)) {
                    // If the username and password are not found in the .password file,
                    // respond with 403 Forbidden
                    return ErrorResponse.createErrorResponse(403, null);
                }
            } catch (IOException e) {
                return ErrorResponse.createErrorResponse(500, null);
            }
        }
        return null;
    }

    private static boolean approved(File passwordFile, String entry) throws IOException {
        // Check the username and password against the contents of the .password file
        String passwordFileContents = new String(Files.readAllBytes(passwordFile.toPath()));
        String[] passwordFileLines = passwordFileContents.split("\n");
        for (String line : passwordFileLines) {
            if (line.equals(entry)) {
                return true;
            }
        }
        return false;
    }
}

