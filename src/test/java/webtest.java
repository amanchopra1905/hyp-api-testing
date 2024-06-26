import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.json.Json;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static io.restassured.RestAssured.given;

public class webtest
{
    RemoteWebDriver driver = null;
    public static String status = "passed";
    public static String username = System.getenv("LT_USERNAME");
    public static String access_key = System.getenv("LT_ACCESS_KEY");

    public static String session_id = null;
    String testURL = "https://todomvc.com/examples/react/#/";
    JSONObject json = new JSONObject();
    String testURLTitle = "React • TodoMVC";
    final static String url="http://demo.guru99.com/V4/sinkministatement.php?CUSTOMER_ID=68195&PASSWORD=1234!&Account_No=1";


    @BeforeMethod
    @Parameters(value={"browser","version","platform"})
    public void testSetUp(String browser, String version, String platform) throws Exception
    {
        String platformName = System.getenv("HYPEREXECUTE_PLATFORM") != null ? System.getenv("HYPEREXECUTE_PLATFORM") : platform;

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("build", "[HyperExecute - 1] Demonstration of the TestNG Framework");
        capabilities.setCapability("name", "[HyperExecute - 1] Demonstration of the TestNG Framework");
        capabilities.setCapability("platform", System.getenv("HYPEREXECUTE_PLATFORM"));
        capabilities.setCapability("browserName", browser);
        capabilities.setCapability("version", version);

        capabilities.setCapability("tunnel",false);
        capabilities.setCapability("network",true);
        capabilities.setCapability("console",true);
        capabilities.setCapability("visual",true);

        try {
            driver = new RemoteWebDriver(new URL("https://" + username + ":" + access_key + "@hub.lambdatest.com/wd/hub"), capabilities);
        }
        catch (MalformedURLException e)
        {
            System.out.println("Invalid grid URL");
        }

        session_id = String.valueOf(driver.getSessionId());
        System.out.println("Session_id : "+session_id);
        json.put("session_id", session_id);
        try (PrintWriter out = new PrintWriter(new FileWriter("session.json"))) {
            out.write(json.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Started session");
    }

    @Test(description="To execute an API test")
    public void apitest() throws InterruptedException
    {

        given().when().get(url).then().log()
                .all();

        given().queryParam("CUSTOMER_ID","68195")
                .queryParam("PASSWORD","1234!")
                .queryParam("Account_No","1") .when().get("http://demo.guru99.com/V4/sinkministatement.php").then().log().body();
        int statusCode= given().queryParam("CUSTOMER_ID","68195")
                .queryParam("PASSWORD","1234!")
                .queryParam("Account_No","1")
                .when().get("http://demo.guru99.com/V4/sinkministatement.php").getStatusCode();
        System.out.println("The response status is "+statusCode);

        given().when().get(url).then().assertThat().statusCode(200);


        /* Once you are outside this code, the list would be empty */
    }


    @AfterMethod
    public void tearDown()
    {
        if (driver != null)
        {
            ((JavascriptExecutor) driver).executeScript("lambda-status=" + status);
            driver.quit();
        }
    }
}
