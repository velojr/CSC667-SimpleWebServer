package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.config.MimeTypes;
import server.request.RequestHandler;

public class WebServer implements AutoCloseable {

    private ServerSocket serverSocket;
    private String documentRoot;
    private ExecutorService executorService;

    public static void main(String[] args) throws NumberFormatException, Exception {
        if (args.length != 2) {
            System.err.println("usage: java WebServer <port number> <document root>");
            System.exit(1);
        }

        try (WebServer server = new WebServer(
                Integer.parseInt(args[0]),
                args[1], MimeTypes.getDefault())) {
            server.listen();
        }
    }

    public WebServer(int port, String documentRoot, MimeTypes mimeTypes) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.documentRoot = documentRoot;
        this.executorService = Executors.newCachedThreadPool();
        //this.executorService = Executors.newFixedThreadPool(10);
        System.out.println("Web server started on port " + port);
    }

    /**
     * After the webserver instance is constructed, this method will be
     * called to begin listening for requestd
     */
    public void listen() {

        // Feel free to change this logic
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler(socket, documentRoot, MimeTypes.getDefault());
                executorService.execute(requestHandler);
                //new Thread(requestHandler).start();
            } catch (IOException e) {
                System.out.println("Error accepting connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() throws Exception {
        this.serverSocket.close();
    }
}