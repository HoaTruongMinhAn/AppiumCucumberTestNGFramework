package com.qa.runners;

import com.qa.stepdef.Hooks;
import com.qa.utils.DriverManager;
import com.qa.utils.GlobalConstants;
import com.qa.utils.GlobalParams;
import com.qa.utils.ServerManager;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.apache.logging.log4j.ThreadContext;
import org.testng.annotations.*;

/**
 * An example of using TestNG when the test class does not inherit from
 * AbstractTestNGCucumberTests but still executes each scenario as a separate
 * TestNG test.
 */
@CucumberOptions(
        plugin = {"pretty"
//                , "html:target/cucumber/SamsungA55/report.html"
                , "summary"
//                , "json:target/cucumber-reports/SamsungA55-cucumber.json"
        }
        , features = {"src/test/resources"}
        , glue = {"com.qa.stepdef"}
        , dryRun = false
        , monochrome = true
//        , strict = true
        , tags = "@test")
public class MySamsungA55TestNGRunnerTest {
    private TestNGCucumberRunner testNGCucumberRunner;

    @Parameters({"emulator", "platformName", "uuid", "deviceName", "systemPort", "chromeDriverPort", "wdaLocalPort", "webkitDebugProxyPort"})
    @BeforeClass(alwaysRun = true)
    public void setUpClass(@Optional("androidOnly") String emulator, String platformName, String uuid, String deviceName, @Optional("androidOnly") String systemPort, @Optional("androidOnly") String chromeDriverPort, @Optional("iOSOnly") String wdaLocalPort, @Optional("iOSOnly") String webkitDebugProxyPort) throws Exception {
        //Set cucumber json output
        System.setProperty("cucumber.plugin", "json:target/cucumber-reports/cucumber-" + deviceName + ".json");

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
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
        System.out.println("##################### beforeClass");
    }


    @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
    public void scenario(PickleWrapper pickle, FeatureWrapper cucumberFeature) {
        testNGCucumberRunner.runScenario(pickle.getPickle());
    }

    @DataProvider
    public Object[][] scenarios() {
        return testNGCucumberRunner.provideScenarios();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        DriverManager driverManager = new DriverManager();
        if (driverManager.getDriver() != null) {
            driverManager.getDriver().quit();
            driverManager.setDriver(null);
        }
        ServerManager serverManager = new ServerManager();
        if (serverManager.getServer() != null) {
            serverManager.getServer().stop();
        }

        if (testNGCucumberRunner != null) {
            testNGCucumberRunner.finish();
        }

        Hooks.cucumberJsonUpdate(GlobalConstants.PROJECT_PATH + GlobalConstants.SEPARATOR + "target\\cucumber-reports\\cucumber-" + new GlobalParams().getDeviceName() + ".json", new GlobalParams().getDeviceName());
    }

}