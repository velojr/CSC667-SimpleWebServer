package server.request;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import server.config.MimeTypes;
import server.response.ErrorResponse;
import server.response.Response;
import server.response.ResponseFactory;

/**
 * A Runnable that handles a single HTTP request.
 */
public class RequestHandler implements Runnable {

    private final Socket socket;
    private final String documentRoot;
    private final MimeTypes mimeTypes;

    public RequestHandler(Socket socket, String documentRoot, MimeTypes mimeTypes) {
        this.socket = socket;
        this.documentRoot = documentRoot;
        this.mimeTypes = mimeTypes;
    }

    @Override
    public void run() {
        try {
            // Read the request from the socket
            Request request = new Request(this.socket.getInputStream());
            request.read();

            // Handle the request
            Response response = handleRequest(request);

            // Send the response back to the client
            if (response != null) {
                OutputStream outputStream = this.socket.getOutputStream();
                response.write(outputStream);
                outputStream.flush();
            } else {
                System.err.println("Failed to generate a response.");
            }

            // Close the socket
            socket.close();

        } catch (IOException e) {
            System.err.println("Request handling error: " + e.getMessage());
        }
    }

    /**
     * Handle the request based on the request method.
     *
     * @param request The request to handle
     * @return The response to send back to the client
     */
    private Response handleRequest(Request request) {
        if (request.getMethod() == null) {
            return handleUnknownRequest();
        }

        try {
            return switch (request.getMethod()) {
                case "GET" -> handleGetRequest(request);
                case "POST" -> handlePostRequest(request);
                case "HEAD" -> handleHeadRequest(request);
                case "PUT" -> handlePutRequest(request);
                case "DELETE" -> handleDeleteRequest(request);
                default -> handleUnknownRequest();
            };
        } catch (Exception e) {
            System.err.println("Internal Server Error: " + e.getMessage());
            return ErrorResponse.createErrorResponse(500, "Internal Server Error");
        }
    }

    /**
     * Handle a GET request.
     *
     * @param request The request to handle
     * @return The response to send back to the client
     */
    private Response handleGetRequest(Request request) {
        System.out.println("Handling GET request for path: " + request.getPath());

        Response response = ResponseFactory.createResponse(this.documentRoot, request);
        if (response == null) {
            return ErrorResponse.createErrorResponse(500, "Internal Server Error");
        }

        return response;
    }

    /**
     * Handle a POST request.
     *
     * @param request The request to handle
     * @return The response to send back to the client
     */
    private Response handlePostRequest(Request request) {
        System.out.println("Handling POST request with body: " 
                + Arrays.toString(request.getBody()));

        Response response = ResponseFactory.createResponse(this.documentRoot, request);
        if (response == null) {
            return ErrorResponse.createErrorResponse(500, "Internal Server Error");
        }

        return response;
    }

    /**
     * Handle a HEAD request.
     *
     * @param request The request to handle
     * @return The response to send back to the client
     */
    private Response handleHeadRequest(Request request) {
        System.out.println("Handling HEAD request for path: " + request.getPath());

        Response response = ResponseFactory.createResponse(this.documentRoot, request);
        if (response == null) {
            return ErrorResponse.createErrorResponse(500, "Internal Server Error");
        }

        return response;
    }

    /**
     * Handle a PUT request.
     *
     * @param request The request to handle
     * @return The response to send back to the client
     */
    private Response handlePutRequest(Request request) {
        System.out.println("Handling PUT request for path: " + request.getPath());

        Response response = ResponseFactory.createResponse(this.documentRoot, request);
        if (response == null) {
            return ErrorResponse.createErrorResponse(500, "Internal Server Error");
        }

        return response;
    }

    /**
     * Handle a DELETE request.
     *
     * @param request The request to handle
     * @return The response to send back to the client
     */
    private Response handleDeleteRequest(Request request) {
        System.out.println("Handling DELETE request for path: " + request.getPath());

        Response response = ResponseFactory.createResponse(this.documentRoot, request);
        if (response == null) {
            return ErrorResponse.createErrorResponse(500, "Internal Server Error");
        }

        return response;
    }

    /**
     * Handle an unknown request method.
     *
     * @return The response to send back to the client
     */
    private Response handleUnknownRequest() {
        System.out.println("Handling unknown request method");
        return ErrorResponse.createErrorResponse(405, "Method Not Allowed");
    }

}