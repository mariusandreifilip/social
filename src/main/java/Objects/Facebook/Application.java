package Objects.Facebook;

import Api.API;
import Api.ApiUtils.RequestUtil;
import Api.ResponseObject;
import Utils.ConfigConstants;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static Objects.Facebook.AppUri.FACEBOOK_GRAPH;

public class Application {

    public static final String id = "108956750076425";

    private static final HashMap<String, String> prms = new HashMap<>();

    JSONObject accounts;
    List<Page> pageList;

    public Application() {

        pageList = new ArrayList<>();
        accounts = new JSONObject();
        accounts = getAppAccounts(this.id);
        generatePageObjectList();

    }


    public JSONObject getAppAccounts(String id) {

        JSONObject result = new JSONObject();
        prms.clear();
        prms.put("access_token", ConfigConstants.STAGE1_TOKEN);

        ResponseObject apiResponse = API.executeAPI(RequestUtil.HttpType.GET, FACEBOOK_GRAPH + id + "/accounts", prms, null, null);

        try {
            accounts = JSONObject.fromObject(apiResponse.getBody());
        } catch (JSONException e) {
            result.put("response", apiResponse.getBody());

        }


        return accounts;
    }


    private void generatePageObjectList() {
        JSONObject pageObject = new JSONObject();


        JSONArray pageArray = (JSONArray) this.accounts.get("data");

        for (Object o : pageArray) {
            if (JSONObject.fromObject(o) != null) {
                pageObject = JSONObject.fromObject(o);
                String access_token = pageObject.getString("access_token");
                String name = pageObject.getString("name");
                String id= pageObject.get("id").toString();
                Page page = new Page(access_token, name, Long.parseLong(id));
        pageList.add(page);

            }

        }


    }


    public List<Page> returnPageList() {

        return pageList;
    }


    public Page getPage(String name) {

        Page pageObject = null;

        for (Page page : pageList) {

            if (page.getName().equals( name)) {
                pageObject = page;

            }

        }
        return pageObject;
    }


}