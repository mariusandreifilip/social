package Api;

import Api.ApiUtils.RequestUtil;
import Utils.JsonUtil;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.HashMap;
import java.util.logging.Logger;

import static Api.ApiConstants.ApiConstants.PROTOCOL;


public class API {

    private static final HashMap<String, String> prms = new HashMap<>();
    private static final Logger log = Logger.getLogger(String.valueOf(API.class));

    public static ResponseObject executeAPI( RequestUtil.HttpType type, String api, HashMap<String, String> params, String payload,
                                            HashMap<String, String> additionalHeaders) {
        String url;
        HttpUriRequest request;
        ResponseObject response;
        int status, repeatNr = 0;
        String payloadLog, payLoadFinal = "", bodyFinal = "";

        do {
            url = RequestUtil.generateURL(PROTOCOL, "443",api, params);
            request = RequestUtil.generateRequest(type, url, payload);
            log.info("Execute request...");
            response = RequestUtil.executeRequest(request);
            status = response.getStatusCode();
            repeatNr++;

        } while (status == 0 && repeatNr < 2);

        if ((status == 200 || status == 201)){

            payloadLog = payload != null ? payload : "NO PAYLOAD";
            payLoadFinal = JsonUtil.getPrettyJson(payloadLog);
            bodyFinal = JsonUtil.getPrettyJson(response.getBody());

            log.info("\n\t" + "API [method] " + request.getMethod()  + "\n\t" + "API [request] " + request.getURI()
                    + "\n\t" + "API [payload] " + payLoadFinal
                    + "\n\t" + "API [status] " + status
                    + "\n\t" + "API [headers] " + additionalHeaders + "\n\t"
                    + "API [response] " + bodyFinal + "\n");
        }
        return response;
    }


}
