package Objects.Facebook.Webhooks;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SerializeWebhook {






        public static void main(String[] args) {

            //generateSha1ForReviewPost();
            //generateSha1ForReviewCommentsAndReplies();
            //generateSha1ForMentionCommentsAndReplies();
            //generateSha1ForMentionsTaggedEndpointPosts();
            //generateSha1ForIGCommentWebhook();
            //generateSha1MentionCommentsInvalidMessage();
            //generateSha1MentionCommentsInvalidPostId();
            //generateSha1generateSha1ForFBMessagesStandbyWebhookForInvalidIGCommentWebhook();
            //generateSha1ForFBMessagesWebhook();
            //generateSha1ForFBMessagesEchosWebhook();
            generateSha1ForFBMessagesStandbyWebhook();
        }

        public static void generateSha1ForReviewPost() {

            //Id of the user/page who was mentioned
            String id = "1332698180228710";

            String reviewer_id = "1071906252988189";
            String reviewer_name = "Mihai Supertramp";
            String comment_id = null;
            String review_text = "Hi I'm Mihai and `i like you!";
            String recommendation_type = "POSITIVE";
            String open_graph_story_id = "1188840347961445";
            String field = "ratings";
            String time = "1553002709";
            String created_time = "1553002706";

            String webhookPayload = "{\"entry\": [{\"changes\": [{\"field\": \"" + field + "\",\"value\": {\"reviewer_id\": \"" + reviewer_id
                    + "\",\"reviewer_name\": \"" + reviewer_name + "\",\"comment_id\": " + comment_id + ",\"review_text\": \"" + review_text
                    + "\",\"recommendation_type\": \"" + recommendation_type + "\",\"item\": \"rating\",\"verb\": \"add\",\"created_time\": " + created_time
                    + ",\"open_graph_story_id\": \"" + open_graph_story_id + "\"} } ],\"id\": \"" + id + "\",\"time\": " + time + "}],\"object\": \"page\"}";
            // Compute the SHA1 for the post body for each of the staging environments.  Select the one you wish to use
            // from the test output.

            System.out.println(webhookPayload + "\n\n");


            Map<String, String> appKeys = new HashMap<>();
            appKeys.put("FB review pots for Sha1 for QA1", "d78cf05c0256b7c5f418946524dd3aa6");
            appKeys.put("FB review pots for Sha1 for QA2", "7fa0b36764797f11da2178bced776d7a");
            appKeys.put("FB review pots for Sha1 for STAG", "936c9044b0f2dc6bd665f276f1119528");

            for (String appKey : appKeys.keySet()){
                String digest = hmacDigest(webhookPayload, appKeys.get(appKey));
                System.out.println(appKey + ": " + digest);
            }
        }

        public static void generateSha1ForReviewCommentsAndReplies() {

            //Id of the user/page who was reviewed
            String id = "1332698180228710";

            //The id of the post
            String post_id = "204568073845716";

            //Id of the user which has reviewed the page
            String sender_id = "119131482389376";

            //Name of the user which has created the review
            String sender_name = "Peach MacDonald";

            //Review message
            String review_text = "salut sont eu, Picasso";

            //parent post id
            String parent_id = "204568073845716";

            //The id of the open_graph_story_id
            String open_graph_story_id = "204568073845716";

            //The id of the comment
            String comment_id = "204568073845716_204568240512366";


            String time = "1551448718";
            String created_time = "1551448716";

            String webhookPayload = "{\"entry\": [{\"changes\": [{\"field\": \"ratings\",\"value\": {\"post_id\": \"" + post_id + "\",\"sender_name\": \"" + sender_name + "\",\"comment_id\": \"" + comment_id + "\",\"sender_id\": \"" + sender_id + "\",\"review_text\": \"" + review_text + "\",\"item\": \"comment\",\"verb\": \"add\",\"parent_id\": \"" + parent_id + "\",\"created_time\": " + created_time + ",\"open_graph_story_id\": \"" + open_graph_story_id + "\"} } ],\"id\": \"" + id + "\",\"time\": " + time + "}],\"object\": \"page\"}";
            System.out.println(webhookPayload + "\n\n");        // Compute the SHA1 for the post body for each of the staging environments.  Select the one you wish to use
            // from the test output.
            Map<String, String> appKeys = new HashMap<>();
            appKeys.put("FB review comments and replies Sha1 for QA1", "d78cf05c0256b7c5f418946524dd3aa6");
            appKeys.put("FB review comments and replies Sha1 for QA2", "7fa0b36764797f11da2178bced776d7a");
            appKeys.put("FB review comments and replies Sha1 for STAG", "936c9044b0f2dc6bd665f276f1119528");

            for (String appKey : appKeys.keySet()){
                String digest = hmacDigest(webhookPayload, appKeys.get(appKey));
                System.out.println(appKey + ": " + digest);
            }
        }

        public static void generateSha1ForMentionsTaggedEndpointPosts() {

            //Id of the user/page who was mentioned
            String id = "1332698180228710";

            //Id of post in which it was mentioned
            String post_id = "2128869687352614_2290200841219497";

            //Post message
            String message = "hello there Short Term Page!";


            String field = "mention";
            String time = "1547135242";
            String created_time = String.valueOf((new Date().getTime())/1000);

            String webhookPayload = "{\"entry\": [{\"changes\": [{\"field\": \"" + field + "\",\"value\": {\"created_time\": " + created_time + ",\"post_id\": \"" + post_id + "\",\"message\": \"" + message + "\",\"verb\": \"add\",\"item\": \"post\"} } ],\"id\": \"" + id + "\",\"time\": " + time + "}],\"object\": \"page\"}";
            System.out.println("Post body: \n" + webhookPayload);
            // Compute the SHA1 for the post body for each of the staging environments.  Select the one you wish to use
            // from the test output.
            Map<String, String> appKeys = new HashMap<>();
            appKeys.put("FB tagged endpoint mention post Sha1 for QA1", "d78cf05c0256b7c5f418946524dd3aa6");
            appKeys.put("FB tagged endpoint mention post Sha1 for QA2", "7fa0b36764797f11da2178bced776d7a");
            appKeys.put("FB tagged endpoint mention post Sha1 for STAG", "936c9044b0f2dc6bd665f276f1119528");

            for (String appKey : appKeys.keySet()){
                String digest = hmacDigest(webhookPayload, appKeys.get(appKey));
                System.out.println(appKey + ": " + digest);
            }
        }

        public static void generateSha1ForMentionCommentsAndReplies() {

            //Id of the user/page who was mentioned
            String id = "434279690461902";

            //Id of post in which the comment with the mention appears
            String post_id = "2128869687352614_2290903647815883";

            //Id of the comment in which it was mentioned
            String comment_id = "2290903647815883_2290904207815827";

            //Comment message
            String message = "hello Adina's new temporary page I'm good. you?";

            String field = "mention";
            String time = "1547133454";
            String created_time = String.valueOf((new Date().getTime())/1000);

            String webhookPayload = "{\"entry\":[ {\"changes\":[{\"field\":\"" + field + "\", \"value\":{\"item\":\"comment\", \"comment_id\":\"" + comment_id + "\", \"post_id\":\"" + post_id + "\", \"verb\":\"add\", \"created_time\":"+ created_time + ", \"message\":\"" + message +"\"} }], \"id\":\"" + id + "\", \"time\":"+ time + "}], \"object\":\"page\"}";
            System.out.println("Post body: \n" + webhookPayload);
            // Compute the SHA1 for the post body for each of the staging environments.  Select the one you wish to use
            // from the test output.
            Map<String, String> appKeys = new HashMap<>();
            appKeys.put("FB mention comments and replies Sha1 for QA1", "d78cf05c0256b7c5f418946524dd3aa6");
            appKeys.put("FB mention comments and replies Sha1 for QA2", "7fa0b36764797f11da2178bced776d7a");
            appKeys.put("FB mention comments and replies Sha1 for STAG", "936c9044b0f2dc6bd665f276f1119528");

            for (String appKey : appKeys.keySet()){
                String digest = hmacDigest(webhookPayload, appKeys.get(appKey));
                System.out.println(appKey + ": " + digest);
            }
        }

        public static void generateSha1ForIGCommentWebhook() {

            //instagram_business_account id of the user who owns the post
            String id = "17841408584919207";
            //String id = "18070599157075838";
            //String id = "1838049228";
            //String id = "2060524463";

            //Id of the comment/reply
            String comment_id = "666688161919101";

            //Comment/reply message
            String message = "testing as v1 webhook";

            String time = String.valueOf((new Date().getTime())/1000);

            String webhookPayload = "{\"entry\":[{\"changes\":[{\"field\":\"comments\",\"value\":{\"text\":\"" + message + "\",\"id\":\"" + comment_id + "\"} }],\"id\":\"" + id + "\",\"time\":" + time + "}],\"object\":\"instagram\"}";
            System.out.println("Post body: \n" + webhookPayload);
            // Compute the SHA1 for the post body for each of the staging environments.  Select the one you wish to use
            // from the test output.
            Map<String, String> appKeys = new HashMap<>();
            appKeys.put("Webhook for IG comment QA1", "d78cf05c0256b7c5f418946524dd3aa6");
            appKeys.put("Webhook for IG comment QA2", "7fa0b36764797f11da2178bced776d7a");
            appKeys.put("Webhook for IG comment STAG", "936c9044b0f2dc6bd665f276f1119528");

            for (String appKey : appKeys.keySet()){
                String digest = hmacDigest(webhookPayload, appKeys.get(appKey));
                System.out.println(appKey + ": " + digest);
            }
        }


        public static void generateSha1ForInvalidIGCommentWebhook() {

            //instagram_business_account id of the user who owns the post
            String id = "17841408584919207";

            //Id of the comment/reply
            String comment_id = "17850720196457699";

            //Comment/reply message
            String message = "engage now";

            String time = String.valueOf((new Date().getTime())/1000);

            String webhookPayload = "{\"entry\":[{\"changes\":[{\"field\":\"comments\",\"value\":{\"text\":\"" + message + "\",\"id\":\"" + comment_id + "\"} }],\"id\":\"" + id + "\",\"time\":" + time + "}],\"object\":\"instagram\"}";
            System.out.println("Post body: \n" + webhookPayload);
            // Compute the SHA1 for the post body for each of the staging environments.  Select the one you wish to use
            // from the test output.
            Map<String, String> appKeys = new HashMap<>();
            appKeys.put("Webhook for IG for invalid comment QA1", "d78cf05c0256b7c5f418946524dd3aa6");
            appKeys.put("Webhook for IG for invalid comment QA2", "7fa0b36764797f11da2178bced776d7a");
            appKeys.put("Webhook for IG for invalid comment STAG", "936c9044b0f2dc6bd665f276f1119528");

            for (String appKey : appKeys.keySet()){
                String digest = hmacDigest(webhookPayload, appKeys.get(appKey));
                System.out.println(appKey + ": " + digest);
            }
        }

        public static void generateSha1MentionCommentsInvalidMessage() {

            //Id of the user/page who was mentioned
            String id = "434279690461902";

            //Id of post in which the comment with the mention appears
            String post_id = "755828918123244_798005230572279";

            //Id of the comment in which it was mentioned
            String comment_id = "798005230572279_798005593905576";

            String field = "mention";
            String time = "1547133454";
            String created_time = String.valueOf((new Date().getTime())/1000);

            String webhookPayload = "{\"entry\":[ {\"changes\":[{\"field\":\"" + field + "\", \"value\":{\"item\":\"comment\", \"comment_id\":\"" + comment_id + "\", \"post_id\":\"" + post_id + "\", \"verb\":\"add\", \"created_time\":"+ created_time + "} }], \"id\":\"" + id + "\", \"time\":"+ time + "}], \"object\":\"page\"}";

            System.out.println("Post body: \n" + webhookPayload);
            // Compute the SHA1 for the post body for each of the staging environments.  Select the one you wish to use
            // from the test output.
            Map<String, String> appKeys = new HashMap<>();
            appKeys.put("FB invalid message mention comments and replies Sha1 for QA1", "d78cf05c0256b7c5f418946524dd3aa6");
            appKeys.put("FB invalid message mention comments and replies Sha1 for QA2", "7fa0b36764797f11da2178bced776d7a");
            appKeys.put("FB invalid message mention comments and replies Sha1 for STAG", "936c9044b0f2dc6bd665f276f1119528");

            for (String appKey : appKeys.keySet()){
                String digest = hmacDigest(webhookPayload, appKeys.get(appKey));
                System.out.println(appKey + ": " + digest);
            }
        }

        public static void generateSha1MentionCommentsInvalidPostId() {

            //Id of the user/page who was mentioned
            String id = "434279690461902";

            //Id of the comment in which it was mentioned
            String comment_id = "61516118118671_71161618181";

            //Comment message
            String message = "This message is a message";

            String field = "mention";
            String time = "1547133454";
            String created_time = String.valueOf((new Date().getTime())/1000);

            String webhookPayload = "{\"entry\":[ {\"changes\":[{\"field\":\"" + field + "\", \"value\":{\"item\":\"comment\", \"comment_id\":\"" + comment_id + "\", \"verb\":\"add\", \"created_time\":"+ created_time + ", \"message\":\"" + message +"\"} }], \"id\":\"" + id + "\", \"time\":"+ time + "}], \"object\":\"page\"}";
            System.out.println("Post body: \n" + webhookPayload);
            // Compute the SHA1 for the post body for each of the staging environments.  Select the one you wish to use
            // from the test output.
            Map<String, String> appKeys = new HashMap<>();
            appKeys.put("FB invalid post_id mention comments and replies Sha1 for QA1", "d78cf05c0256b7c5f418946524dd3aa6");
            appKeys.put("FB invalid post_id mention comments and replies Sha1 for QA2", "7fa0b36764797f11da2178bced776d7a");
            appKeys.put("FB invalid post_id mention comments and replies Sha1 for STAG", "936c9044b0f2dc6bd665f276f1119528");

            for (String appKey : appKeys.keySet()){
                String digest = hmacDigest(webhookPayload, appKeys.get(appKey));
                System.out.println(appKey + ": " + digest);
            }
        }

        public static void generateSha1ForFBMessagesWebhook() {
            //identification data
            String receiverId = "2128869687352614";
            String time = String.valueOf((new Date().getTime())/1000);
            String senderId = "1896414633790869";

            //message
            String mid = "vD4cMmn1M8l_5xMM5MBoc4Kw72wx6GMkAR7UpiISik86Vp5Igtt2J_qolXjmHaXeDrNg";
            String text = "hello!";
            String seq = "0";
            String timeStamp = String.valueOf((new Date().getTime())/1000);

            String webhookPayload = "{\"object\":\"page\",\"entry\":[{\"id\":\"" + receiverId + "\",\"time\":\"" + time + "\",\"messaging\":[{\"sender\":{\"id\":\"" + senderId + "\"},\"recipient\":{\"id\":\"" + receiverId + "\"},\"timestamp\":" + timeStamp + ",\"message\":{\"mid\":\"" + mid + "\",\"text\":\"" + text + "\",\"seq\":" + seq + "}}]}]}";
            System.out.println("Post body: \n" + webhookPayload);

            // Compute the SHA1 for the payload for each of the staging environments. Select the one you wish to use
            // from the test output.
            Map<String, String> appKeys = new HashMap<>();
            appKeys.put("FB message payload Sha1 for QA1", "d78cf05c0256b7c5f418946524dd3aa6");
            appKeys.put("FB message payload Sha1 for QA2", "7fa0b36764797f11da2178bced776d7a");
            appKeys.put("FB message payload Sha1 for STAG", "936c9044b0f2dc6bd665f276f1119528");

            for (String appKey : appKeys.keySet()){
                String digest = hmacDigest(webhookPayload, appKeys.get(appKey));
                System.out.println(appKey + ": " + digest);
            }
        }

        public static void generateSha1ForFBMessagesEchosWebhook() {
            //identification data
            String receiverId = "2128869687352614";
            String time = String.valueOf((new Date().getTime())/1000);
            String senderId = "1896414633790869";
            String appId = "2266088933461611";

            //message
            String mid = "vD4cMmn1M8l_5xMM5MBoc4Kw72wx6GMkAR7UpiISik86Vp5Igtt2J_qolXjmHaXeDrNg";
            String text = "hello!";
            String seq = "0";
            String timeStamp = String.valueOf((new Date().getTime())/1000);

            String webhookPayload = "{\"object\":\"page\",\"entry\":[{\"id\":\"" + receiverId + "\",\"time\":\"" + time + "\",\"messaging\":[{\"sender\":{\"id\":\"" + senderId + "\"},\"recipient\":{\"id\":\"" + receiverId + "\"},\"timestamp\":" + timeStamp + ",\"message\":{\"is_echo\":true,\"app_id\":" + appId + ",\"mid\":\"" + mid + "\",\"seq\":" + seq + ",\"text\":\"" + text + "\"}}]}]}";
            System.out.println("Post body: \n" + webhookPayload);

            // Compute the SHA1 for the payload for each of the staging environments. Select the one you wish to use
            // from the test output.
            Map<String, String> appKeys = new HashMap<>();
            appKeys.put("FB message echo payload Sha1 for QA1", "d78cf05c0256b7c5f418946524dd3aa6");
            appKeys.put("FB message echo payload Sha1 for QA2", "7fa0b36764797f11da2178bced776d7a");
            appKeys.put("FB message echo payload Sha1 for STAG", "936c9044b0f2dc6bd665f276f1119528");

            for (String appKey : appKeys.keySet()) {
                String digest = hmacDigest(webhookPayload, appKeys.get(appKey));
                System.out.println(appKey + ": " + digest);
            }

        }

        public static void generateSha1ForFBMessagesStandbyWebhook() {
            //identification data
            String receiverId = "2128869687352614";
            String time = String.valueOf((new Date().getTime())/1000);
            String senderId = "1896414633790869";


            String webhookPayload = "{\"object\":\"page\",\"entry\":[{\"id\":\"" + receiverId + "\",\"time\":\"" + time + "\",\"standby\":[{\"sender\":{\"id\":\"" + senderId + "\"},\"recipient\":{\"id\":\"" + receiverId + "\"}}]}]}";
            System.out.println("Post body: \n" + webhookPayload);

            // Compute the SHA1 for the payload for each of the staging environments. Select the one you wish to use
            // from the test output.
            Map<String, String> appKeys = new HashMap<>();
            appKeys.put("FB standby message payload Sha1 for QA1", "d78cf05c0256b7c5f418946524dd3aa6");
            appKeys.put("FB standby message payload Sha1 for QA2", "7fa0b36764797f11da2178bced776d7a");
            appKeys.put("FB standby message payload Sha1 for STAG", "936c9044b0f2dc6bd665f276f1119528");

            for (String appKey : appKeys.keySet()) {
                String digest = hmacDigest(webhookPayload, appKeys.get(appKey));
                System.out.println(appKey + ": " + digest);
            }

        }

        private static String hmacDigest(String msg, String keyString) {
            String digest = null;
            try {
                SecretKeySpec key = new SecretKeySpec((keyString).getBytes(StandardCharsets.UTF_8), "HmacSHA1");
                Mac mac = Mac.getInstance("HmacSHA1");
                mac.init(key);

                byte[] bytes = mac.doFinal(msg.getBytes(StandardCharsets.US_ASCII));

                StringBuilder hash = new StringBuilder();
                for (byte aByte : bytes) {
                    String hex = Integer.toHexString(0xFF & aByte);
                    if (hex.length() == 1) {
                        hash.append('0');
                    }
                    hash.append(hex);
                }
                digest = hash.toString();
            } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            }
            return digest;
        }
    }






