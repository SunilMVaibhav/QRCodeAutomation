package com.lambdatest;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import net.glxn.qrgen.javase.QRCode;
import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import javax.imageio.ImageIO;
import javax.swing.text.html.ImageView;
import java.io.*;

import java.net.URL;
import java.util.Base64;

import static io.appium.java_client.android.nativekey.AndroidKey.R;

public class Setup {
    public AndroidDriver<AndroidElement> driver;
    protected String mediaUrl;
    public String gridURL = "@mobile-hub.lambdatest.com/wd/hub";
    String username = "YOUR-USERNAME";
    String accessKey = "YOUR-ACCESS-KEY";

    String credentials = username + ":" + accessKey;
    String basicAuth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());

    @BeforeMethod
    public void beforeMethod() throws Exception
    {

        String filetoUploads =  generateQRCodeImage("https://www.chatgpt.com/");
        mediaUrl= uploadImageAndGetUrl(filetoUploads);
        System.out.println(mediaUrl);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        //capabilities.setCapability("build", "debug");
        capabilities.setCapability("build","QRCode-Test-Android");
        capabilities.setCapability("name", "Android_Camera_injection_test");
        capabilities.setCapability("deviceName", "Pixel 9");
        capabilities.setCapability("platformVersion", "14");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("autoAcceptAlert", true);
        capabilities.setCapability("autoGrantPermission", true);
        capabilities.setCapability("isRealMobile", true);
        //AppURL (Create from Wikipedia.apk sample in project)
        capabilities.setCapability("app", "APP-ID"); //Enter your app url
        capabilities.setCapability("deviceOrientation", "PORTRAIT");
        capabilities.setCapability("console", true);
        capabilities.setCapability("network", false);
        capabilities.setCapability("visual", true);
        capabilities.setCapability("devicelog", true);
        capabilities.setCapability("enableImageInjection",true);
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        capabilities.setCapability("unicodeKeyboard", true);
        capabilities.setCapability("resetKeyboard", true);
        capabilities.setCapability("idleTimeout",200);




        String hub = "https://" + username + ":" + accessKey + gridURL;
        driver = new AndroidDriver<AndroidElement>(new URL(hub), capabilities);

       //driver = new AndroidDriver<AndroidElement>(new URL("https://"+username+":"+accessKey+"@mobile-hub.lambdatest.com/wd/hub"), capabilities);
    }

    @AfterMethod(alwaysRun=true)
    public void tearDown() throws Exception
    {
        driver.quit();
    }


    public static String generateQRCodeImage(String barcodeText) throws Exception {
        ByteArrayOutputStream stream = QRCode
                .from(barcodeText)
                .withSize(1012, 1012)
                .stream();

        ByteArrayInputStream bis = new ByteArrayInputStream(stream.toByteArray());
        String outputFileName = System.currentTimeMillis()+".png";
        IOUtils.copy(bis, new FileOutputStream(outputFileName));//Writing image from stream memory to file
        return outputFileName;
    }


    public static void main(String[] args) throws Exception {
      String fileName =   generateQRCodeImage("https://www.chatgpt.com/");

    }





    private String uploadImageAndGetUrl(String fileToUploade) throws IOException {


        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("media_file",fileToUploade,
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(fileToUploade)))//QR Code file path
                .addFormDataPart("type","image")
                .addFormDataPart("custom_id","my-photo")
                .build();

        Request request = new Request.Builder()
                .url("https://mobile-mgm.lambdatest.com/mfs/v1.0/media/upload")
                .method("POST", body)
                .addHeader("Authorization", basicAuth)//encode cred for both username & api key
                .build();
        Response response = client.newCall(request).execute();
        String jsonString = response.body().string();

        System.out.println("Response :--"+jsonString);

        JSONObject jo = new JSONObject(jsonString);
        return (String) jo.get("media_url");
    }
}
