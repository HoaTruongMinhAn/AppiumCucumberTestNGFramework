package com.qa.stepdef;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qa.pages.BasePage;
import com.qa.utils.DriverManager;
import com.qa.utils.GlobalParams;
import com.qa.utils.VideoManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;

import java.io.File;
import java.io.IOException;

public class Hooks {


    public static void cucumberJsonUpdate(String jsonFilePath, String deviceName) {
//        String jsonFilePath = "target/cucumber-reports/cucumber.json";
//        String deviceName = System.getProperty("deviceName", "UnknownDevice");
        System.out.println("jsonFilePath: " + jsonFilePath);
        System.out.println("deviceName: " + deviceName);

        File file = new File(jsonFilePath);
        if (!file.exists()) {
            System.err.println("Error: JSON file does not exist");
            return;
        }
        if (file.length() == 0) {
            System.err.println("Error: JSON file is empty.");
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));
            System.out.println("rootNode: " + rootNode);

            for (JsonNode feature : rootNode) {
                System.out.println("feature: " + feature);
                for (JsonNode scenario : feature.get("elements")) {
                    System.out.println("JSON has elements");
                    if (scenario.has("name")) {
                        System.out.println("JSON has name");
                        String originalName = scenario.get("name").asText();
                        String updatedName = originalName + " [Device: " + deviceName + "]";
                        ((ObjectNode) scenario).put("name", updatedName);
                        System.out.println("originalName: " + originalName);
                        System.out.println("updatedName: " + updatedName);
                    }
                }
            }

            objectMapper.writeValue(new File(jsonFilePath), rootNode);
            System.out.println("Updated cucumber.json successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void initialize(Scenario scenario) throws Exception {
        BasePage basePage = new BasePage();
        basePage.closeApp();
        basePage.launchApp();

//        System.out.println("##################### beforeScenario");
//        String deviceName = new GlobalParams().getDeviceName();
//        String updatedScenarioName = scenario.getName() + " [Device: " + deviceName + "]";

//        Field nameField = scenario.getClass().getDeclaredField("name");
//        nameField.setAccessible(true);
//        nameField.set(scenario, updatedScenarioName);

//        GlobalParams params = new GlobalParams();
//        params.initializeGlobalParams();
//
//        ThreadContext.put("ROUTINGKEY", params.getPlatformName() + "_"
//                + params.getDeviceName());
//
//        new ServerManager().startServer();
//        new DriverManager().initializeDriver();
        new VideoManager().startRecording();
    }

    @After
    public void quit(Scenario scenario) throws IOException {
        byte[] video = new VideoManager().stopRecording(scenario.getName());

        if (scenario.isFailed()) {
            byte[] screenshot = new DriverManager().getDriver().getScreenshotAs(OutputType.BYTES);

            //TODO reduce attachment size
            scenario.attach(screenshot, "image/png", scenario.getName());
            scenario.attach(video, "video/mp4", "Video" + scenario.getName());
        }

        System.out.println("##################### afterScenario");
        String deviceName = new GlobalParams().getDeviceName();
        scenario.attach("Device: " + deviceName, "text/plain", "Device Info");

//        System.out.println("##################### cucumberJsonUpdate ");
//        cucumberJsonUpdate(GlobalConstants.PROJECT_PATH + GlobalConstants.SEPARATOR + "target\\cucumber-reports\\cucumber-" + deviceName + ".json", deviceName);


/*        DriverManager driverManager = new DriverManager();
        if(driverManager.getDriver() != null){
            driverManager.getDriver().quit();
            driverManager.setDriver(null);
        }
        ServerManager serverManager = new ServerManager();
        if(serverManager.getServer() != null){
            serverManager.getServer().stop();
        }*/
    }

}