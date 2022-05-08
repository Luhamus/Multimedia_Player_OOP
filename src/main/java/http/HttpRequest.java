package http;
import java.util.HashMap;

public class HttpRequest extends HttpMessage{
    private HttpMethod method;
    private String requestTarget;
    private String originalHttpVersion; //requesti literaal
    private HttpVersion bestCompatibleHttpVersion;
    private final HashMap<String, String> headers = new HashMap<>();

    public HttpRequest() {
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public HttpVersion getBestCompatibleHttpVersion() {
        return bestCompatibleHttpVersion;
    }

    public String getOriginalHttpVersion() {
        return originalHttpVersion;
    }

    public HashMap<String, String>  getHeaders() { return headers; }

    void setMethod(String methodName) throws HttpParsingException {
        for (HttpMethod method : HttpMethod.values()){
            if (methodName.equals(method.name())){
                this.method = method;
                return;
            }
        }
        throw new HttpParsingException(
                HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED
        );
    }

    void setRequestTarget(String requestTarget) throws HttpParsingException {
        if (requestTarget == null || requestTarget.length() == 0){
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }
        this.requestTarget = requestTarget;
    }


    public void setHttpVersion(String originalHttpVersion) throws BadHttpVersionException, HttpParsingException {
        this.originalHttpVersion = originalHttpVersion;
        this.bestCompatibleHttpVersion = HttpVersion.getBestCompatibleVersion(originalHttpVersion);
        if(this.bestCompatibleHttpVersion == null){
            throw new HttpParsingException(
                    HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED
            );
        }
    }

    /**
     * Set request headers
     *
     * @param headerLine line of string from header with key and value that needs splitting and formatting
     */
    public void setHeaders(String headerLine) {
        String[] headerLineArr=(headerLine.split(":"));
        this.headers.put(headerLineArr[0].strip(), headerLineArr[1].strip());

    }

}
