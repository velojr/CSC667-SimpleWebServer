package server.request;

import java.util.HashMap;
import java.util.Map;

public class ParsedRequest {

    private String path;
    private final Map<String, String> queryMap = new HashMap<>();
    private final Map<String, String> headerMap = new HashMap<>();
    private final Map<String, String> cookieMap = new HashMap<>();
    private String method;
    private byte[] body;

    public String getQueryParam(String key) {
        return queryMap.getOrDefault(key, null);
    }

    public String getHeaderValue(String key) {
        return headerMap.getOrDefault(key, null);
    }

    public void setQueryParam(String key, String value) {
        queryMap.put(key, value);
    }

    public void setHeaderValue(String key, String value) {
        headerMap.put(key, value);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setCookieValue(String key, String value) {
        cookieMap.put(key, value);
    }

    public String getCookieValue(String key) {
        return cookieMap.getOrDefault(key, null);
    }
}
