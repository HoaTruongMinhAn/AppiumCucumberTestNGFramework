package com.qa.utils;

import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class CapabilitiesManager {
    TestUtils utils = new TestUtils();

    public DesiredCapabilities getCaps() throws IOException {
        GlobalParams params = new GlobalParams();
        Properties props = new PropertyManager().getProps();

        try {
            utils.log().info("getting capabilities");
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", params.getPlatformName());
            caps.setCapability("udid", params.getUDID());
            caps.setCapability("deviceName", params.getDeviceName());

            switch (params.getPlatformName()) {
                case "Android":
                    caps.setCapability("appium:automationName", props.getProperty("androidAutomationName"));
                    caps.setCapability("appium:appPackage", props.getProperty("androidAppPackage"));
                    caps.setCapability("appium:appActivity", props.getProperty("androidAppActivity"));
                    caps.setCapability("systemPort", params.getSystemPort());
                    caps.setCapability("appium:chromeDriverPort", params.getChromeDriverPort());
                    //String androidAppUrl = getClass().getResource(props.getProperty("androidAppLocation")).getFile();
                    String androidAppUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                            + File.separator + "resources" + File.separator + "apps" + File.separator + "Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";
                    utils.log().info("appUrl is" + androidAppUrl);
                    caps.setCapability("appium:app", androidAppUrl);
                    break;
                case "iOS":
                    caps.setCapability("automationName", props.getProperty("iOSAutomationName"));
                    //String iOSAppUrl = getClass().getResource(props.getProperty("iOSAppLocation")).getFile();
                    String iOSAppUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                            + File.separator + "resources" + File.separator + "apps" + File.separator + "SwagLabsMobileApp.app";
                    utils.log().info("appUrl is" + iOSAppUrl);
                    caps.setCapability("bundleId", props.getProperty("iOSBundleId"));
                    caps.setCapability("wdaLocalPort", params.getWdaLocalPort());
                    caps.setCapability("webkitDebugProxyPort", params.getWebkitDebugProxyPort());
                    caps.setCapability("app", iOSAppUrl);
                    break;
            }
            return caps;
        } catch (Exception e) {
            e.printStackTrace();
            utils.log().fatal("Failed to load capabilities. ABORT!!" + e.toString());
            throw e;
        }
    }

    public UiAutomator2Options getUiAutomator2Options() throws Exception {
        GlobalParams params = new GlobalParams();
        Properties props = new PropertyManager().getProps();
        UiAutomator2Options options = new UiAutomator2Options();

        //Set options
        options.setPlatformName(params.getPlatformName());
        options.setDeviceName(params.getDeviceName());
        options.setUdid(params.getUDID());
        options.setAutomationName(props.getProperty("androidAutomationName"));

        //Ser avd for emulator
//        if (params.getEmulator().equals("true")) {
//            options.setAvd(params.getDeviceName());
//        }
        options.setSystemPort(Integer.parseInt(params.getSystemPort()));
        options.setChromedriverPort(Integer.parseInt(params.getChromeDriverPort()));


        //Install
        String appShortURL = props.getProperty("androidAppLocation");
        String appUrl = System.getProperty("user.dir") + GlobalConstants.SEPARATOR + "src" + GlobalConstants.SEPARATOR + "test" + GlobalConstants.SEPARATOR + "resources" + GlobalConstants.SEPARATOR + appShortURL;
        options.setApp(appUrl);
        utils.log().info("appUrl is: " + appUrl);

        //App package
        options.setAppPackage(props.getProperty("androidAppPackage"));
        options.setAppActivity(props.getProperty("androidAppActivity"));

        //Unlock option
        options.setUnlockType(props.getProperty("androidUnlockType"));
        options.setUnlockKey(props.getProperty("androidUnlockKey"));

        //Permission
        options.autoGrantPermissions();

        //Set timeout
//                options.setAvdLaunchTimeout(Duration.ofSeconds(180));
//                options.setAvdReadyTimeout(Duration.ofSeconds(180));
        options.setNewCommandTimeout(Duration.ofSeconds(300));

        return options;
    }

    public XCUITestOptions getXCUITestOptions() throws Exception {
        GlobalParams params = new GlobalParams();
        Properties props = new PropertyManager().getProps();
        XCUITestOptions iOSOptions = new XCUITestOptions();

        iOSOptions.setAutomationName(props.getProperty("iOSAutomationName"));

        //Install
        String iOSAppUrl = System.getProperty("user.dir") + GlobalConstants.SEPARATOR + "src" + GlobalConstants.SEPARATOR + "test"
                + GlobalConstants.SEPARATOR + "resources" + GlobalConstants.SEPARATOR + "app" + GlobalConstants.SEPARATOR + "SwagLabsMobileApp.app";
        iOSOptions.setApp(iOSAppUrl);
        utils.log().info("appUrl is: " + iOSAppUrl);

        iOSOptions.setBundleId(props.getProperty("iOSBundleId"));
        iOSOptions.setWdaLocalPort(Integer.parseInt(params.getWdaLocalPort()));
//                    iOSOptions.setWebKitDebugProxyPort(27753);
        iOSOptions.setCapability("webkitDebugProxyPort", params.getWebkitDebugProxyPort());


        //Unlock option
        iOSOptions.setCapability("unlockType", "passcode"); // Use "passcode" or "biometric"
        iOSOptions.setCapability("unlockKey", "000000");

        //Permission
        iOSOptions.setCapability("permissions", "location=always");
//                    iOSOptions.autoAcceptAlerts();
        iOSOptions.setAutoAcceptAlerts(true);


        //Set timeout
//                options.setAvdLaunchTimeout(Duration.ofSeconds(180));
//                options.setAvdReadyTimeout(Duration.ofSeconds(180));
        iOSOptions.setNewCommandTimeout(Duration.ofSeconds(300));

        return iOSOptions;
    }
}
