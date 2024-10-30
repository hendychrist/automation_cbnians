package com.cbn.cbnians;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.Optional;
import io.appium.java_client.AppiumBy;

import io.appium.java_client.android.AndroidDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class LoginTest { // Make sure this class is public

    public static void main(String[] args) { 
            
            // Set the Desired Capabilities
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", "Android");
            caps.setCapability("platformVersion", "13.0");
            caps.setCapability("udid", "RRCT301X2GN"); 
            caps.setCapability("deviceName", "Samsung Device"); 
            caps.setCapability("appPackage", "com.cbn.cbnians"); 
            caps.setCapability("appActivity", "com.cbn.cbnians.MainActivity"); 
            caps.setCapability("noReset", true); 
            caps.setCapability("newCommandTimeout", 300);
            caps.setCapability("automationName", "uiautomator2");
            caps.setCapability("app", "/Users/hendychristian/Desktop/project/flutter/Cbnians/cbnianscombined/build/app/outputs/apk/debug/app-debug.apk");
      
        try {
            // Initialize the AndroidDriver
            AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);

            WebElement getStartButton = null;
            WebElement privacyPolicyButton = null;

            try{
                // privacyPolicyButton = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Privacy Policy\"]"));
                privacyPolicyButton = driver.findElement(AppiumBy.accessibilityId("Privacy Policy"));

                System.out.println("");
                System.out.println("----------------------------------------------------------------------------------");
                System.out.println("SUCCESS - (AppiumBy).accessibilityId -> Found -> 'Privacy Policy' button");

            }catch(Exception e){
                System.out.println("ERROR - Not Found 'Privacy Policy' button");
                System.out.println("----------------------------------------------------------------------------------");
                System.out.println("");
            }

            if(privacyPolicyButton != null){
                privacyPolicyButton.click();

                System.out.println("SUCCESS - Click 'Privacy Policy' button - in Login Screen");
                System.out.println("");

                @SuppressWarnings("unused")
                WebElement openWebView = new FluentWait<>(driver)
                                                        .withTimeout(Duration.ofSeconds(20)) 
                                                        .pollingEvery(Duration.ofMillis(500))
                                                        .ignoring(NoSuchElementException.class) 
                                                        .until(drv -> {
                                                                    WebElement element = driver.findElement(By.xpath("//android.widget.TextView[@text=\"Privacy Policy\"]"));

                                                                    if(element != null){
                                                                        System.out.println("");
                                                                        System.out.println("SUCCESS - Open Webview and found 'Privacy Policy' Text - in WebView Page");

                                                                        return element;
                                                                    }else{
                                                                        System.out.println("");
                                                                        System.out.println("ERROR - FAILED to Open Webview and Not found 'Privacy Policy' Text");
                                                                        System.out.println("----------------------------------------------------------------------------------");
                                                                        System.out.println("");

                                                                        return null;
                                                                    }   
                                                                });

                    if(openWebView != null){
                        Optional.ofNullable(driver.findElement(By.xpath("//android.widget.Button"))).ifPresent(WebElement::click);
                        System.out.println("SUCCESS - Click Back Button on Widget AppBar - in WebView Page");
                        System.out.println("----------------------------------------------------------------------------------");
                    }
                                                                
            }else{
                System.out.println("ERROR - 'Privacy Policy' Cannot be CLICKED().");
                System.out.println("----------------------------------------------------------------------------------");
                System.out.println("");
            }
    
            try {
                getStartButton = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Get Started\"]"));
                System.out.println("");
                System.out.println("----------------------------------------------------------------------------------");
                System.out.println("SUCCESS - Found 'Get Started' button.");
            } catch (Exception e) {
                System.out.println("ERROR - 'Get Started' button not found by ID, trying Accessibility ID.");
                System.out.println("----------------------------------------------------------------------------------");
                System.out.println("");
            }

            if (getStartButton != null) {
                getStartButton.click();

                System.out.println("SUCCESS - Click 'Get Started' button");
                System.out.println("----------------------------------------------------------------------------------");
                System.out.println("");
                
                String usernameXpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.widget.EditText[1]";

                WebElement usernameTextField = new FluentWait<>(driver)
                                                .withTimeout(Duration.ofSeconds(10)) 
                                                .pollingEvery(Duration.ofMillis(500))
                                                .ignoring(NoSuchElementException.class) 
                                                .until(drv -> {
                                                            WebElement element = driver.findElement(By.xpath(usernameXpath));
                                                            System.out.println("");
                                                            System.out.println("----------------------------------------------------------------------------------");
                                                            System.out.println("SUCCESS - Show Keyboard when Click TextField");
                                                            return element;
                                                        });

                if(usernameTextField != null){
                    usernameTextField.click();
                    System.out.println("SUCCESS - CLICK() usernameTextField");
                    
                    // input text
                    driver.findElement(By.xpath(usernameXpath)).sendKeys("hendy.christian");
                    System.out.println("SUCCESS - CLICK() usernameTextField input Text username");
                    
                    
                    // usernameTextField.sendKeys("hendy.christian"); - error
                    // System.out.println("SUCCESS - INPUT VALUE() keys to send - hendy.christian");
                    
                    System.out.println("----------------------------------------------------------------------------------");

                }else{
                    System.out.println("ERROR - CLICK() usernameTextField");
                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.println("");
                }
                                                                 
        
            } else {
                System.out.println("ERROR - 'Get Started' Cannot be CLICKED().");
                System.out.println("----------------------------------------------------------------------------------");
                System.out.println("");
            }

            driver.quit();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
   
    }
}
