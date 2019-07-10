package Objects.Facebook.Webhooks;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Date;

public class MessageWebhook {


    JSONObject payload;
    String id;

//    , String senderId, String mid, String text, String seq

    public MessageWebhook(String receiverId) {

        this.payload = new JSONObject();
        generateWebhook(receiverId);


    }

    public String getId() {

        return id;
    }

    public JSONObject generateWebhook(String receiverId) {
        //String webhookPayload = "{\"object\":\"page\",\"entry\":[{\"id\":\"" + receiverId
        // + "\",\"time\":\"" + time + "\",\"messaging\":[{\"sender\":{\"id\":\"" + senderId + "\"},
        // \"recipient\":{\"id\":\"" + receiverId + "\"},\"timestamp\":" + timeStamp + ",\"message\":
        // {\"mid\":\"" + mid + "\",\"text\":\"" + text + "\",\"seq\":" + seq + "}}]}]}";

        JSONArray entry = new JSONArray();
        JSONArray messaging = new JSONArray();
        JSONObject message = new JSONObject();


        JSONObject entryObject = new JSONObject();


        entryObject.put("id", receiverId);
        entryObject.put("time", new Date().getTime());
        entryObject.put("messaging", messaging);
        entry.add(entryObject);
        messaging.add(message);
        message.put("sender", "sender");
        this.payload.put("entry", entry);
        this.payload.put("object", "page");


        return payload;

    }

    public String getPayloadAsString() {
        return String.valueOf(payload);
    }

    public static void main(String args[]) {

        MessageWebhook a = new MessageWebhook("234");
        System.out.println(JSONObject.fromObject(a));


    }
}

