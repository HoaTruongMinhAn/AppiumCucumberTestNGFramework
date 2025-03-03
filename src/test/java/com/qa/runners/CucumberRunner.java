package com.qa.runners;

import com.qa.utils.GlobalParams;
import com.qa.utils.TestUtils;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.lang.reflect.Field;

public class CucumberRunner {
    @Before
    public void cucumberBeforeScenario(Scenario scenario) {
        System.out.println("##################### updatedScenarioName ");
        String deviceName = new GlobalParams().getDeviceName();
        String updatedScenarioName = scenario.getName() + " [Device: " + deviceName + "]";
        new TestUtils().log().info("updatedScenarioName " + updatedScenarioName);

        // Using reflection to update the scenario name
        try {
            Field nameField = scenario.getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(scenario, updatedScenarioName);
            new TestUtils().log().info("Already set scenario name as:  " + updatedScenarioName);
            System.out.println("Already set scenario name as:  " + updatedScenarioName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
