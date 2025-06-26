package server.tools;

import server.config.MimeTypes;

public class MimeTypeUtils {
    public static String getMimeTypeFromExtension(String path) {
        return MimeTypes.getDefault().getMimeTypeFromExtension(getExtension(path));
    }

    public static String getExtension(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex == -1) {
            return "";
        }
        return path.substring(dotIndex + 1);
    }
}
