package com.qa.runners;

import com.qa.utils.DriverManager;
import com.qa.utils.GlobalParams;
import com.qa.utils.ServerManager;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.apache.logging.log4j.ThreadContext;
import org.testng.annotations.*;

import java.lang.reflect.Field;

/**
 * An example of using TestNG when the test class does not inherit from
 * AbstractTestNGCucumberTests but still executes each scenario as a separate
 * TestNG test.
 */
@CucumberOptions(
        plugin = {"pretty"
//                , "html:target/cucumber/report.html"
                , "summary"
//                , "json:target/cucumber-reports/cucumber.json"
        }
        , features = {"src/test/resources"}
        , glue = {"com.qa.stepdef"}
        , dryRun = false
        , monochrome = true
//        , strict = true
        , tags = "@test")
public class MyTecnoTestNGRunnerTest {

    private TestNGCucumberRunner testNGCucumberRunner;

    @Parameters({"emulator", "platformName", "uuid", "deviceName", "systemPort", "chromeDriverPort", "wdaLocalPort", "webkitDebugProxyPort"})
    @BeforeClass(alwaysRun = true)
    public void setUpClass(@Optional("androidOnly") String emulator, String platformName, String uuid, String deviceName, @Optional("androidOnly") String systemPort, @Optional("androidOnly") String chromeDriverPort, @Optional("iOSOnly") String wdaLocalPort, @Optional("iOSOnly") String webkitDebugProxyPort) throws Exception {
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
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        String deviceName = new GlobalParams().getDeviceName();
        String updatedScenarioName = scenario.getName() + " [Device: " + deviceName + "]";

        // Using reflection to update the scenario name
        try {
            Field nameField = scenario.getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(scenario, updatedScenarioName);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    }

}