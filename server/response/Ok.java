package server.response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Ok extends Response {

    public Ok(String documentRoot) {
        //super(documentRoot);
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        String body = "<html><body><h1>It works!</h1></body></html>";

        writer.write("HTTP/1.1 200 OK\r\n");
        writer.write("Content-Type: text/html\r\n");
        writer.write(String.format("Content-Length: %d\r\n", body.length()));
        writer.write("\r\n");
        writer.write(body.toCharArray());

        writer.flush();
        writer.close();
    }

}