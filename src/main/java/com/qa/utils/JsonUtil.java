package com.qa.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class JsonUtil {
    TestUtils utils = new TestUtils();

    //Update Json, add device name after scenario name and method name
    public void addDeviceName(String jsonFilePath, String deviceName) {
        //TODO update scenario name: "Login scenarios" + device name
        System.out.println("########### start addDeviceName");
        System.out.println("jsonFilePath " + jsonFilePath);
        File file = new File(jsonFilePath);
        if (!file.exists()) {
            utils.log().error("Error: JSON file does not exist");
            return;
        }
        if (file.length() == 0) {
            utils.log().error("Error: JSON file is empty.");
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));


//            objectMapper.getFactory().setStreamReadConstraints(StreamReadConstraints.builder().maxStringLength(30000000).build());
//            utils.log().info("########## newFile");
//            File newFile = new File(jsonFilePath);
//
//            utils.log().info("########## objectMapper.readTree");
//            JsonNode rootNode = objectMapper.readTree(newFile);

            for (JsonNode feature : rootNode) {
                //replace scenario
                System.out.println("########### replace scenario");
                String originalScenarioName = feature.get("name").asText();
                String updatedScenarioName = originalScenarioName + " [Device: " + deviceName + "]";
                ((ObjectNode) feature).put("name", updatedScenarioName);

                //replace method name
                for (JsonNode scenario : feature.get("elements")) {
                    if (scenario.has("name")) {
                        System.out.println("########### replace method");
                        String originalName = scenario.get("name").asText();
                        String updatedName = originalName + " [Device: " + deviceName + "]";
                        ((ObjectNode) scenario).put("name", updatedName);
                    }
                }
            }
            objectMapper.writeValue(new File(jsonFilePath), rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
