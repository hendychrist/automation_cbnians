package com.cbn.cbnians;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class LoginTest {

    public static void main(String[] args) {
        AndroidDriver driver = null;
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        DesiredCapabilities caps = setDesiredCapabilities();

        try {
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);
            WebDriverWait wait20 = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebDriverWait wait10 = new WebDriverWait(driver, Duration.ofSeconds(10));

            if (isUserLoggedIn(driver, wait20)) {
                System.out.println("SECOND FLOW - User already logged in, proceeding with post-login flow.");
                proceedAfterLogin(driver, wait20, wait10);
            } else {
                // Flow Sebelum Login
                System.out.println("FIRST FLOW - NOT FOUND CBN logo on Homepage - cbnLogoKiriAtas");   
            
                clickPrivacyPolicy(driver, wait20);
                clickGetStarted(driver, wait20);
                enterCredentials(driver, wait20);
                loginButton(driver, wait20);
                proceedAfterLogin(driver, wait20, wait10);
            }

            executorService.schedule(() -> {
                // Your scheduled tasks here
            }, 0, TimeUnit.SECONDS);

            driver.quit();
        } catch (MalformedURLException e) {
            System.out.println("ERROR - Malformed URL: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERROR - Unexpected error: " + e.getMessage());
        } finally {
            wait20ForActiveThreads();

            // Ensure driver is not null before calling quit
            if (driver != null) {
                driver.quit();
                System.out.println("SUCCESS - Driver quit successfully");
            } else {
                System.out.println("WARNING - Driver was never initialized");
            }
            
            // Properly shutdown executor service
            shutdownExecutorService(executorService);
            checkRemainingThreads();
        }
    }

    private static DesiredCapabilities setDesiredCapabilities() {
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
        caps.setCapability("app", "/Users/hendychristian/Desktop/project/flutter/cbnians-flavor/build/app/outputs/flutter-apk/app-prod-release.apk");
        return caps;
    }

    private static void proceedAfterLogin(AndroidDriver driver, WebDriverWait wait20, WebDriverWait wait10) {
        checkInButton(driver, wait20);
        dialogNotice(driver, wait10);
    }

    private static void clickPrivacyPolicy(AndroidDriver driver, WebDriverWait wait20) {
        try {
            WebElement privacyPolicyButton = wait20.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("Privacy Policy")));
            privacyPolicyButton.click();
            System.out.println("SUCCESS - Clicked 'Privacy Policy' button");
            navigateBackToLogin(driver, wait20);
        } catch (Exception e) {
            System.out.println("ERROR - 'Privacy Policy' button not found or clickable");
            System.exit(0);  
        }
    }

    private static void clickGetStarted(AndroidDriver driver, WebDriverWait wait20) {
        try {
            WebElement getStartButton = wait20.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.view.View[@content-desc='Get Started']")));
            getStartButton.click();
            System.out.println("SUCCESS - Clicked 'Get Started' button");
        } catch (Exception e) {
            System.out.println("ERROR - 'Get Started' button not found or clickable");
        }
    }

    private static void navigateBackToLogin(AndroidDriver driver, WebDriverWait wait20) {
        try {
            @SuppressWarnings("unused")
            WebElement openWebView = wait20.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.TextView[@text='Privacy Policy']")));
            WebElement buttonBack = driver.findElement(By.xpath("//android.widget.Button"));
            if(buttonBack != null){
                buttonBack.click();
            }

            System.out.println("SUCCESS - Clicked back button on WebView page");
        } catch (NoSuchElementException e) {
            System.out.println("ERROR - WebView Back button not found");
        }
    }

    private static void enterCredentials(AndroidDriver driver, WebDriverWait wait20) {
        String usernameXpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.widget.EditText[1]";
        String passwordXpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.widget.EditText[2]";

        WebElement usernameTextField = driver.findElement(By.xpath(usernameXpath));
        wait20.until(ExpectedConditions.presenceOfElementLocated(By.xpath(usernameXpath)));

        if (usernameTextField != null) {
            usernameTextField.click();
            System.out.println("SUCCESS - CLICK() usernameTextField");
            driver.findElement(By.xpath(usernameXpath)).sendKeys("hendy.christian");
            System.out.println("SUCCESS - SENDKEYS() usernameTextField input Text username");
        } else {
            System.out.println("ERROR - usernameTextField is null");
        }

        WebElement passwordTextField = driver.findElement(By.xpath(passwordXpath));
        if (passwordTextField != null) {
            passwordTextField.click();
            System.out.println("SUCCESS - CLICK() passwordTextField");
            driver.findElement(By.xpath(passwordXpath)).sendKeys("jv@3qz%bc5");
            System.out.println("SUCCESS - SENDKEYS() passwordTextField - input value");
        } else {
            System.out.println("ERROR - passwordTextField is null");
        }
    }

    private static void shutdownExecutorService(ScheduledExecutorService executorService) {
        executorService.shutdown(); // Initiate shutdown
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // Force shutdown if not terminated
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt(); // Preserve interrupt status
        }
    }

    private static void checkRemainingThreads() {
        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);
        for (Thread thread : threads) {
            if (thread != null && thread.isAlive()) {
                System.out.println("WARNING: Thread " + thread.getName() + " is still running.");
                thread.interrupt(); // Optionally interrupt remaining threads
            }
        }
    }

    private static void wait20ForActiveThreads() {
        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);
        for (Thread thread : threads) {
            if (thread != null && thread.isAlive()) {
                try {
                    thread.join(2000); // wait20 for up to 2 seconds
                    if (thread.isAlive()) {
                        System.out.println("WARNING: Thread " + thread.getName() + " is still alive after wait20ing.");
                        // Optional: Decide how to handle long-lived threads
                    }
                } catch (InterruptedException e) {
                    System.out.println("Interrupted while wait20ing for thread: " + thread.getName());
                    Thread.currentThread().interrupt(); // Restore interrupted status
                    break; // Exit the loop if interrupted
                }
            }
        }
    }
    
    private static void loginButton(AndroidDriver driver, WebDriverWait wait20) {
        try {
            WebElement loginButton = wait20.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Login")));
            loginButton.click();
            System.out.println("SUCCESS - Clicked login button");

            wait20.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ScrollView/android.widget.ImageView[1]")));
            System.out.println("SUCCESS - Found CBN logo on Homepage");
        } catch (Exception e) {
            System.out.println("ERROR - Login button or homepage elements not found");
        }
    }

    private static void checkInButton(AndroidDriver driver, WebDriverWait wait20) { 
        try{
            WebElement checkInIcon = wait20.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("Check In")));
            System.out.println("SUCCESS - Found Check In Icon");
            checkInIcon.click();

        }catch(Exception e){
            System.out.println("ERROR - Check In Icon in HomePage Not Found");
        }
    }

    private static void dialogNotice(AndroidDriver driver, WebDriverWait wait10){
        try{
            WebElement allowLocation = wait10.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("Privacy Notice")));
            
            if(allowLocation != null){
                System.out.println("SUCCESS - FOUND dialog privacy Notice Location CHECK IN");
                clickAllowDialog(driver, wait10);
            }else{
                System.out.println("SUCCESS - NOUT FOUND dialog privacy Notice Location CHECK IN");
            }
            
        }catch(Exception e){
            // Check In Icon without dialog PRIVACY NOTICE - LOCATION
            System.out.println("CATCH - ERROR - Not FOund Privacy Notice Dialog");
            selectWorkingSpace(driver,wait10);
        }
     }
       
    private static boolean isUserLoggedIn(AndroidDriver driver, WebDriverWait wait20) {
        try {
            wait20.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ScrollView/android.widget.ImageView[1]"))).click();;
            System.out.println("SUCCESS - Found CBN logo on Homepage");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void clickAllowDialog(AndroidDriver driver, WebDriverWait wait10){
        try{

            WebElement allowButton = wait10.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("ALLOW")));
            if(allowButton != null){
                allowButton.click();
                System.out.println("SUCCESS - Click ALLOW in Flutter Dialog");
                allowPermissionLocation(driver, wait10);
            }else{
                System.out.println("ERROR - Not Able to Click ALLOW in Flutter Dialog");
            }
            
        }catch(Exception e){
            System.out.println("ERROR - Not Found Allow Flutter Dialog");
        }
     }
               
    private static void allowPermissionLocation(AndroidDriver driver, WebDriverWait wait10){
        try{

            WebElement allowButton = driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"com.android.permissioncontroller:id/permission_allow_foreground_only_button\"]"));
            if(allowButton != null){
                allowButton.click();
                System.out.println("SUCCESS - Click ALLOW in SAMSUNG Dialog");
                selectWorkingSpace(driver, wait10);
            }else{
                System.out.println("SUCCESS - Click ALLOW in SAMSUNG Dialog");
            }
            
        }catch(Exception e){
            System.out.println("ERROR - Not Found Allow Button IN Notice Dialog");
        }
     }

    private static void selectWorkingSpace(AndroidDriver driver, WebDriverWait wait10){
        try{

            WebElement selectWorkingSpace = wait10.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("Cyber 2 Tower 33rd Floor")));
            if(selectWorkingSpace != null){
                selectWorkingSpace.click();
                System.out.println("SUCCESS - Click ALLOW in SAMSUNG Dialog");
                // Lanjut disini
            }else{
                System.out.println("SUCCESS - Click ALLOW in SAMSUNG Dialog");
            }
            
        }catch(Exception e){
            System.out.println("ERROR - Not Found Allow Button IN Notice Dialog");
        }
     }

    
}
