package server.config;

import java.util.HashMap;
import java.util.Map;

public class MimeTypes {

    public static MimeTypes getDefault() {
        MimeTypes mimeTypes = new MimeTypes();

        mimeTypes.addMimeType("png", "image/png");
        mimeTypes.addMimeType("jpg", "image/jpg");
        mimeTypes.addMimeType("jpeg", "image/jpg");
        mimeTypes.addMimeType("txt", "text/plain");
        mimeTypes.addMimeType("html", "text/html");
        mimeTypes.addMimeType("htm", "text/html");
        mimeTypes.addMimeType("css", "text/css");
        mimeTypes.addMimeType("js", "text/javascript");
        mimeTypes.addMimeType("json", "application/json");
        // If additional mime types are needed, _valid_ mime types can be added here

        return mimeTypes;
    }

    private Map<String, String> mimeTypes;

    public MimeTypes() {
        this.mimeTypes = new HashMap<>();
    }

    public String getMimeTypeFromExtension(String extension) {
        return this.mimeTypes.get(extension);
    }

    public void addMimeType(String extension, String mimeType) {
        this.mimeTypes.put(extension, mimeType);
    }

}
