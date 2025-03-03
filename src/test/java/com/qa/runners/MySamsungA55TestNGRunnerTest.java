package com.qa.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.testng.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * An example of using TestNG when the test class does not inherit from
 * AbstractTestNGCucumberTests but still executes each scenario as a separate
 * TestNG test.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"
//                , "html:target/cucumber/SamsungA55/report.html"
                , "summary"
                , "json:target/cucumber-reports/SamsungA55-cucumber.json"
        }
        , features = {"src/test/resources"}
        , glue = {"com.qa.stepdef"}
        , dryRun = false
        , monochrome = true
//        , strict = true
//        , tags = "@test"
)
public class MySamsungA55TestNGRunnerTest extends RunnerBase {

}