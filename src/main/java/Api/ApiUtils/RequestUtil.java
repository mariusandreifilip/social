package Api.ApiUtils;

import Api.ResponseObject;
import Utils.SignConstants;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Logger;

public class RequestUtil {
    private static final Logger log = Logger.getLogger(String.valueOf(RequestUtil.class));
    // HTTP Method types
    public static enum HttpType {
        GET, POST, HEAD, DELETE, PUT
    }
    public static HttpUriRequest generateRequest(HttpType httpType,
                                                 String requestURL, String... requestBody) {
        HttpUriRequest httpRequest=null;

        switch (httpType) {
            case POST: {
                HttpPost post = new HttpPost(requestURL);
                if (requestBody != null && requestBody.length > 0) {
                    try {
                        post.setEntity(new StringEntity(requestBody[0], "UTF-8"));
                        post.setHeader("Content-Type", "application/json;charset=UTF-8");
                    } catch (UnsupportedEncodingException e) {
                    }
                }
                httpRequest = post;
                break;
            }
//            case DELETE: {
//                MyHttpDelete deleteWithBody = new MyHttpDelete(requestURL);
//                if (requestBody != null && requestBody.length > 0) {
//                    try {
//                        deleteWithBody.setEntity(new StringEntity(requestBody[0], TestConstants.UTF_8));
//                        deleteWithBody.setHeader("Content-Type",
//                                "application/json;charset=UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                    }
//                }
//
//                httpRequest = deleteWithBody;
//                break;
//            }
            case GET: {
                httpRequest = new HttpGet(requestURL);
                break;
            }
            case HEAD: {
                httpRequest = new HttpHead(requestURL);
                break;
            }

            case PUT: {
                HttpPut put = new HttpPut(requestURL);

                if (requestBody != null) {
                    try {
                        put.setEntity(new StringEntity(requestBody[0], "UTF-8"));
                        put.setHeader("Content-Type",
                                "application/json;charset=UTF-8");
                    } catch (UnsupportedEncodingException e) {
                    }
                }
                httpRequest = put;
                break;
            }
        }
        // TODO set headers if available

        return httpRequest;
    }
    public static ResponseObject executeRequest(HttpUriRequest request) {
        BasicClientConnectionManager cm = new BasicClientConnectionManager();
        HttpClient client = new DefaultHttpClient(cm);
//        TestUtils.testSleep(TestConstants.WAIT_INTERVAL_API);
        HttpResponse httpResponse = null;
        ResponseObject responseObject = new ResponseObject();

        try {
            httpResponse = client.execute(request);
        } catch (ClientProtocolException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if (httpResponse != null) {
            try {
                responseObject.setHeaders(httpResponse.getAllHeaders());
                responseObject.setStatusCode(httpResponse.getStatusLine()
                        .getStatusCode());
                if (httpResponse.getEntity() != null)
                    responseObject.setBody(EntityUtils.toString(httpResponse.getEntity()));
            } catch (ParseException | IOException | NullPointerException e) {
            }
        }
        cm.shutdown();
        return responseObject;
    }

    public static String generateURL(String protocol, String port,
                                     String uri, HashMap<String, String> paramsMap) {

        String returnURL = "";
        String portString = "";
        if (!"".equals(port)) {
            portString += (":" + port);
        }

        returnURL += (protocol + "://" + uri);
        if (paramsMap != null) {

            boolean firstParam = true;
            for (String key : paramsMap.keySet()) {
                if (firstParam) {
                    returnURL += "".equals(key) ? ("?" + paramsMap.get(key))
                            : ("?" + key + SignConstants.EQUAL_SIGN + paramsMap.get(key).replaceAll(" ", "%20"));
                    firstParam = false;
                } else {
                    returnURL += ("&" + key + SignConstants.EQUAL_SIGN + paramsMap.get(key));
                }
            }
        }

        log.info("Executing HTTP call to: " + returnURL);
        return returnURL;

    }


}
