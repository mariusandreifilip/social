package Utils;

import Api.ApiConstants.ApiConstants;
import Api.ResponseObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.http.Header;
import org.apache.log4j.Logger;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

import java.util.*;

public class JsonUtil {

    private static final Logger log = Logger.getLogger(JsonUtil.class);

    /**
     * Returns a value from a JSON field extracted from the http response; assumes the response contains a single JSON
     * and not an array of JSONS
     *
     * @param response - the http response object
     * @param key      - the field name from the json
     * @return the value of the key field as string
     */
    public static String getValueFromResponse(ResponseObject response,
                                              String key) {
        String value = null;
        try {
            JSONObject json = getJson(response);
            value = json.getString(key);
        } catch (Exception e) {
            log.error("Unable to extract JSON from response: " + e.getMessage());
        }

        return value;
    }

    /**
     * Checks if the list of comet messages contain JSON objects, parses each object and checks that the asserted key
     * has the expected value
     *
     * @param msgs - the list of comet messages
     * @param key  - the asserted key
     * @return TRUE if the key-pair value is found at least once in the list of comet messages, FALSE otherwise
     */
    public static String assertInComet(List<String> msgs, String key) {
        String value = null;

        for (String msg : msgs) {
            // check if the comet message contains a JSON and extract it
            if (msg.contains("{")) {
                msg = msg.substring(msg.lastIndexOf("~") + 1);
                try {
                    value = JSONArray.fromObject(msg).getJSONObject(0)
                            .getString(key);
                    break;
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return null;

                }
            }
        }
        return value;

    }

    private static String getApiResponseTime(ResponseObject response) {
        String date = "";
        if (response != null) {
            for (Header h : response.getHeaders()) {
                if ("Date".equals(h.getName())) {
                    date = h.getValue();
                    break;
                }
            }
        }
        return date;
    }

    /**
     * Extracts a {@link JSONObject} from a {@link ResponseObject}
     *
     * @param response the {@link ResponseObject}
     * @return a {@link JSONObject}
     */
    public static JSONObject getJson(ResponseObject response) {
        JSONObject result = new JSONObject();
        if (response != null && response.getBody() != null) {
            if (!response.getBody().equals("")) {
                try{
                    result = JSONObject.fromObject(response.getBody());
                }catch (JSONException e){
                    log.info("Unable to convert to JSONObject:" + response.toString());
                }
                return result;
            } else {
                return JSONObject.fromObject("{result:'no response'}");
            }
        }
        return result;
    }

    public static JSONObject getLoginJson(ResponseObject response) {
        JSONObject result = getJson(response);
        if (!result.isEmpty()) {
            result.put("platform_time", getApiResponseTime(response));
        }
        return result;
    }

    /**
     * Extracts a {@link JSONArray} from a {@link ResponseObject}
     *
     * @param response the {@link ResponseObject}
     * @return a {@link JSONArray}
     */
    public static JSONArray getJsonArray(ResponseObject response) {
        return JSONArray.fromObject(response.getBody());

    }

    /**
     * @param expected_key in comet msg/json
     * @param ack          - comet msg/json
     * @return expected_key value found in ack
     */
    public static String getKeyValueInJson(String expected_key, Object ack) {
        String expected = "not_found";
        if (ack instanceof JSONObject) {
            JSONObject msg = (JSONObject) ack;
            for (Iterator<String> key = msg.keys(); key.hasNext(); ) {
                String k = key.next();
                if ((!(msg.get(k) instanceof JSONObject)) && (!(msg.get(k) instanceof JSONArray))) {
                    if (expected_key.equals(k)) {
                        expected = msg.get(k).toString();
                        return expected;
                    }
                } else if (expected.equals("not_found")) {
                    expected = getKeyValueInJson(expected_key, msg.get(k));
                }
            }
        } else if (ack instanceof JSONArray) {
            JSONArray expectedArray = (JSONArray) ack;
            for (Object expectedElement : expectedArray) {
                if (expected.equals("not_found")) {
                    expected = getKeyValueInJson(expected_key, expectedElement);
                }
            }
        }
        return expected;
    }

    /**
     * @param exp - expected JSONObject
     * @param act - actual JSONObject
     * @return - true if expected JSON is included in actual JSON, else false is returned
     * @throws JSONException
     */
    public static boolean jsonCompare(Object exp, Object act) throws JSONException {

        if (exp instanceof JSONObject) {
            JSONObject expected = (JSONObject) exp;
            JSONObject actual = (JSONObject) act;
            if (expected.size() > actual.size()) {
                return false;
            }
            for (Iterator<String> key = expected.keys(); key.hasNext(); ) {
                String k = key.next();
                Object expectedFieldValue = expected.get(k);
                Object actualFieldValue = actual.get(k);
                if ((actualFieldValue == null) && (expectedFieldValue != null)) {
                    return false;
                }
                if (!jsonCompare(expectedFieldValue, actualFieldValue)) {
                    return false;
                }
            }
        } else if (exp instanceof JSONArray) {
            JSONArray expectedArray = (JSONArray) exp;
            JSONArray actualArray = (JSONArray) act;
            if (expectedArray.size() > actualArray.size()) {
                return false;
            }
            for (Object expectedElement : expectedArray) {
                boolean matchFound = false;
                for (Object actualElement : actualArray) {
                    if (jsonCompare(expectedElement, actualElement)) {
                        matchFound = true;
                        break;
                    }
                }
                if (!matchFound) {
                    return false;
                }
            }
        } else {
            if (!exp.equals(act)) {
                return false;
            }
        }
        return true;
    }

    public static JSONCompareResult smartJsonCompare(Object exp, Object act) throws org.json.JSONException {

        if (exp instanceof JSONObject && act instanceof JSONObject) {
            JSONObject expected = JSONObject.fromObject(exp);
            JSONObject actual = JSONObject.fromObject(act);
            return JSONCompare.compareJSON(expected.toString(), actual.toString(), JSONCompareMode.LENIENT);
        } else if (exp instanceof JSONArray && act instanceof JSONArray) {
            JSONArray expected = JSONArray.fromObject(exp);
            JSONArray actual = JSONArray.fromObject(act);
            return JSONCompare.compareJSON(expected.toString(), actual.toString(), JSONCompareMode.LENIENT);
        } else {
            return JSONCompare.compareJSON("{result:'unable to parse expected or actual msg'}", "{}", JSONCompareMode.STRICT);

        }

    }

    public static JSONObject removeKeyFromJson(JSONObject actualJson, String extraKey) {
        if (actualJson != null && actualJson.size() > 0 && extraKey != null) {
            for (Iterator<String> k = actualJson.keys(); k.hasNext(); ) {
                String key = k.next();
                // if object is just string we remove the key
                if ((actualJson.optJSONArray(key) == null) && (actualJson.optJSONObject(key) == null)) {
                    if (key.equals(extraKey)) {
                        actualJson.remove(key);
                        return actualJson;
                    }
                }
                // if it's jsonobject
                if (actualJson.optJSONObject(key) != null) {
                    removeKeyFromJson(actualJson.getJSONObject(key), extraKey);
                }
                // if it's jsonarray
                if (actualJson.optJSONArray(key) != null) {
                    JSONArray jArray = actualJson.getJSONArray(key);
                    for (int i = 0; i < jArray.size(); i++) {
                        removeKeyFromJson(jArray.getJSONObject(i), extraKey);
                    }
                }
            }
        }
        return actualJson;
    }



    public static String getPrettyJson(Object rawJson) {
        if (rawJson != null) {
            if (ApiConstants.ARRANGE_MSG) {
                if (isJSON(rawJson)) {
                    return "\n" + JSONObject.fromObject(rawJson).toString(4, 4);
                } else if (isArray(rawJson)) {
                    return "\n" + JSONArray.fromObject(rawJson).toString(4, 4);
                } else {
                    return rawJson.toString();
                    //return "\n\t" + rawJson.toString().replace(OtherConstants.COMMA, OtherConstants.NEW_LINE_NEW_TAB)
                    //    .replace("[", "\n\t\t[\n\t").replace("]", "\n\t\t]");
                }
            } else {
                return rawJson.toString();
            }
        } else {
            return "{}";
        }
    }

    private static boolean isJSON(Object rawJson) {
        try {
            JSONObject.fromObject(rawJson);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    private static boolean isArray(Object rawJson) {
        try {
            JSONArray.fromObject(rawJson);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    public static Object getResponseFromJSONApiCall(JSONObject callResponse) {
        JSONArray arrayValue = callResponse.optJSONArray("response");
        JSONObject jsonValue = callResponse.optJSONObject("response");
        String stringValue = callResponse.optString("response");
        if (arrayValue != null) {
            return arrayValue;
        } else if (jsonValue != null) {
            return jsonValue;
        } else if (stringValue != "") {
            return stringValue;
        } else {
            return JSONObject.fromObject("'response':'invalid_api_response'");
        }

    }

    public static JSONObject replaceKeyValueInJson(String key, String expectedValue, JSONObject actual) {
        String actualValue = actual.optString(key);
        if (!actualValue.equals(expectedValue)) {
            actual.replace(key, actualValue, expectedValue);
        }
        return actual;
    }

    public static JSONArray replaceJSonInArray(JSONObject expectedJson, JSONObject actualJson, JSONArray jsonArray) {
        int pos = jsonArray.lastIndexOf(actualJson);
        jsonArray.remove(actualJson);
        jsonArray.add(pos, expectedJson);
        return jsonArray;
    }










}
