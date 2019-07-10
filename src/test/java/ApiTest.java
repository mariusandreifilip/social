import Api.ResponseObject;
import Objects.Facebook.Application;
import Objects.Facebook.FacebookPost;
import Objects.Facebook.Page;
import org.apache.http.client.methods.HttpUriRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Created by Andrei Filip on 6/8/19.
 */

public class ApiTest extends BasicTest {

    private final HashMap<String, String> loginData = new HashMap<>();
    private static String loginUrl;
    private static ResponseObject response;
    private static HttpUriRequest request;



    @BeforeMethod
    public void setUp() throws Exception {
        loginUrl = "";
        loginData.clear();
        response = new ResponseObject();
    }



    @Test(priority = 1)
    public void getFacebookToken(){
        testCase.setTestCase("00", "Simple Facebook post");

        testCase.addTestStep("Create Application");

        Application a=new Application();

        testCase.addTestStep("Get PageId");

        Page adinaPage= a.getPage("Adina's Delivery");


        testCase.addTestStep("Post Message on  Adina's Delivery FacebookPage");

        FacebookPost post=new FacebookPost(adinaPage.getPageid(),"automationPost",adinaPage.getToken());



//        Application login= new Application(APPID,APP_SECRET);
//        System.out.println(login.getToken());
//           FacebookPost postMessage= new FacebookPost("2128869687352614","automation_test_message",ACCESS_TOKEN
//        );


//        Assert.assertEquals(login.getStatusCode(), HttpStatus.SC_OK,
//                "Login failed. Invalid status code");









    }
}
