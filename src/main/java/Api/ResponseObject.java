package Api;

import org.apache.http.Header;

public class ResponseObject {
    private String responseBody;
    private int responseStatusCode = 0;
    private Header[] responseHeaders = null;

    public String getBody() {
        return responseBody;
    }

    public void setBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return responseStatusCode;
    }

    public void setStatusCode(int responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }

    public Header[] getHeaders() {
        return responseHeaders;
    }

    public void setHeaders(Header[] responseHeaders) {
        this.responseHeaders = responseHeaders;
    }
}
