package com.lambdatest;

import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.IOException;

import static org.testng.AssertJUnit.assertEquals;

public class Test1 extends Setup{
    @org.testng.annotations.Test
    public void cameraInjectionTest() throws InterruptedException, IOException
    {
        WebDriverWait wait = new WebDriverWait(driver, 30);


        //
        MobileElement elementLogin = (MobileElement) wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.EditText[@content-desc=\"test-Username\"]")));
        elementLogin.click();
        elementLogin.sendKeys("standard_user");

        MobileElement elementPW = (MobileElement) wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.EditText[@content-desc=\"test-Password\"]")));
        elementPW.click();
        elementPW.sendKeys("secret_sauce");

        MobileElement elementLoginBtn = (MobileElement) wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"test-LOGIN\"]")));
        elementLoginBtn.click();


        MobileElement hamburgerMenu = (MobileElement) wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"test-Menu\"]")));
        hamburgerMenu.click();


        MobileElement qrCodeScanner = (MobileElement) wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"test-QR CODE SCANNER\"]")));
        qrCodeScanner.click();

        //Clicking on ScanBtn and closing the icon
//        MobileElement allowOption = (MobileElement) wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("com.android.permissioncontroller:id/permission_allow_foreground_only_button")));
//        allowOption.click();

        //Uploading the Image
         driver.executeScript("lambda-image-injection="+mediaUrl);
         Thread.sleep(9000);
//        MobileElement closeScanIcon = (MobileElement)wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.maruti.ibwms_qa:id/close_button")));
//        closeScanIcon.click();
//        Thread.sleep(5000);

        MobileElement appURL = (MobileElement) wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.android.chrome:id/url_bar")));
        Thread.sleep(5000);
        assertEquals( "chatgpt.com", appURL.getText());
        System.out.println("Image got successfully scanned.");
       // driver.navigate().back();
//        MobileElement logoutIcon = (MobileElement)wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"test-LOGOUT\"]")));
//        logoutIcon.click();
//
//        MobileElement logoutYes = (MobileElement)wait.until((ExpectedConditions.visibilityOfElementLocated(By.id("android:id/button1"))));
//        logoutYes.click();


        driver.quit();


    }



}
