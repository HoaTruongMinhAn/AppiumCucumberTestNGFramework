package com.qa.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverManager {
    private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    TestUtils utils = new TestUtils();

    public AppiumDriver getDriver() {
        return driver.get();
    }

    public void setDriver(AppiumDriver driver2) {
        driver.set(driver2);
    }

    public void initializeDriver() throws Exception {
        AppiumDriver driver = null;
        GlobalParams params = new GlobalParams();
        PropertyManager props = new PropertyManager();

        if (driver == null) {
            try {
                utils.log().info("initializing Appium driver");
                switch (params.getPlatformName()) {
                    case "Android":
                        utils.log().info("initializing AndroidDriver");
                        driver = getAndroidDriver();
                        utils.log().info("complete initializing AndroidDriver");
                        break;
                    case "iOS":
//                        driver = new IOSDriver(new ServerManager().getServer().getUrl(), new CapabilitiesManager().getCaps());
                        break;
                }
                if (driver == null) {
                    throw new Exception("driver is null. ABORT!!!");
                }
                utils.log().info("Driver is initialized");
                setDriver(driver);
            } catch (IOException e) {
                e.printStackTrace();
                utils.log().fatal("Driver initialization failure. ABORT !!!!" + e.toString());
                throw e;
            }
        }
    }

    public AppiumDriver getAndroidDriver() throws MalformedURLException {
        AppiumDriver driver;
        URL url = new URL("http://0.0.0.0:4723");
//        case "android":
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");

        options.setDeviceName("Samsung A55");
        options.setUdid("R5CX90YXF5Z");
        options.setAutomationName("UiAutomator2");

        options.setSystemPort(Integer.parseInt("10000"));
        options.setChromedriverPort(Integer.parseInt("11000"));


        //Install
//        String appShortURL = props.getProperty("androidAppLocation");
        String appUrl = "C:\\Users\\minhanhoa.truong\\Automation_Appium\\05 - Cucumber\\AppiumCucumberFramework\\src\\test\\resources\\apps\\Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";
        options.setApp(appUrl);
        utils.log().info("appUrl is: " + appUrl);

        //App package
        options.setAppPackage("com.swaglabsmobileapp");
        options.setAppActivity("com.swaglabsmobileapp.MainActivity");

        //Unlock option
        options.setUnlockType("pin");
        options.setUnlockKey("000000");

        //Permission
        options.autoGrantPermissions();

        //Set timeout
//                options.setAvdLaunchTimeout(Duration.ofSeconds(180));
//                options.setAvdReadyTimeout(Duration.ofSeconds(180));
        options.setNewCommandTimeout(Duration.ofSeconds(300));


        driver = new AndroidDriver(url, options);
        utils.log().info("driver on Before Test: " + driver);
        return driver;
    }


}
