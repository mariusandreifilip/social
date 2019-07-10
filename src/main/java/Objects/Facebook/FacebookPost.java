package Objects.Facebook;

import Api.API;
import Api.ApiUtils.RequestUtil;
import Api.ResponseObject;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.util.HashMap;

import static Objects.Facebook.AppUri.FACEBOOK_GRAPH;


public class FacebookPost {

    private static final HashMap<String, String> prms = new HashMap<>();
    String postId;
    JSONObject postObject;
    JSONObject payload;



//    POST https://graph.facebook.com/546349135390552/feed
//            ?message=Hello Fans!
//            &access_token=your-access-token
//
//





    public FacebookPost(long pageiD, String message,String token){

        postObject=new JSONObject();
        payload=new JSONObject();
        generatePayload(message,token);
        postMessage(pageiD);

    }


    private void generatePayload(String pageAccessToken,String message){
        payload.put("access_token",pageAccessToken);
        payload.put("message",message);


    }




    private JSONObject postMessage(long pageiD){



        JSONObject result = new JSONObject();
        prms.clear();
//        prms.put("access_token", pageAccessToken);
//        prms.put("message", message);


        ResponseObject apiResponse = API.executeAPI(RequestUtil.HttpType.POST, FACEBOOK_GRAPH + "2128869687352614" + "/feed", null, payload.toString(), null);

        try {
            postObject = JSONObject.fromObject(apiResponse.getBody());
        } catch (JSONException e) {
            result.put("response", apiResponse.getBody());

        }

        return postObject;



    }


    public String getPostId(){
        return postId;
    }


}
