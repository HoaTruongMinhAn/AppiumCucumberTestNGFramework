package com.qa.runners;

import com.qa.utils.*;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.apache.logging.log4j.ThreadContext;
import org.testng.annotations.*;

public class RunnerBase {
    private static final ThreadLocal<TestNGCucumberRunner> testNGCucumberRunner = new ThreadLocal<TestNGCucumberRunner>();
    TestUtils utils = new TestUtils();

    public static TestNGCucumberRunner getRunner() {
        return testNGCucumberRunner.get();
    }

    public static void setRunner(TestNGCucumberRunner runner) {
        testNGCucumberRunner.set(runner);
    }

    @Parameters({"emulator", "platformName", "uuid", "deviceName", "systemPort", "chromeDriverPort", "wdaLocalPort", "webkitDebugProxyPort"})
    @BeforeClass(alwaysRun = true)
    public void setUpClass(@Optional("androidOnly") String emulator, String platformName, String uuid, String deviceName, @Optional("androidOnly") String systemPort, @Optional("androidOnly") String chromeDriverPort, @Optional("iOSOnly") String wdaLocalPort, @Optional("iOSOnly") String webkitDebugProxyPort) throws Exception {
        //Set cucumber json output
//        System.setProperty("cucumber.plugin", "json:target/cucumber-reports/cucumber-" + deviceName + ".json");
        System.out.println("################## BeforeClass ");
//        System.out.println("json:target/cucumber-reports/cucumber-" + deviceName + ".json");

        ThreadContext.put("ROUTINGKEY", platformName + "_" + deviceName);
        GlobalParams params = new GlobalParams();
        params.setEmulator(emulator);
        params.setPlatformName(platformName);
        params.setUDID(uuid);
        params.setDeviceName(deviceName);

        switch (platformName.toLowerCase()) {
            case "android":
                params.setSystemPort(systemPort);
                params.setChromeDriverPort(chromeDriverPort);
                break;
            case "ios":
                params.setWdaLocalPort(wdaLocalPort);
                params.setWebkitDebugProxyPort(webkitDebugProxyPort);
                break;
        }

        new ServerManager().startServer();
        new DriverManager().initializeDriver();
        setRunner(new TestNGCucumberRunner(this.getClass()));
    }


    @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
    public void scenario(PickleWrapper pickle, FeatureWrapper cucumberFeature) {
        getRunner().runScenario(pickle.getPickle());
    }

    @DataProvider
    public Object[][] scenarios() {
        return getRunner().provideScenarios();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        System.out.println("################## AfterClass ");
        DriverManager driverManager = new DriverManager();
        if (driverManager.getDriver() != null) {
            driverManager.getDriver().quit();
            driverManager.setDriver(null);
        }

        //Comment this to prevent server stop after each class run. Purpose to solve the issue that Cucumber report cannot generate json file when running mutiple device parallel
        ServerManager serverManager = new ServerManager();
        serverManager.stopCurrentThreadServer();

        if (testNGCucumberRunner != null) {
            getRunner().finish();
        }

        //Update Json, add device name after scenario name and method name
        new JsonUtil().addDeviceName(GlobalConstants.PROJECT_PATH + GlobalConstants.SEPARATOR + "target" + GlobalConstants.SEPARATOR + "cucumber-reports" + GlobalConstants.SEPARATOR + new GlobalParams().getDeviceName() + "-cucumber.json", new GlobalParams().getDeviceName());
    }
}
