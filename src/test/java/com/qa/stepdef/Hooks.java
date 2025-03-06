package com.qa.stepdef;

import com.qa.pages.BasePage;
import com.qa.utils.DriverManager;
import com.qa.utils.GlobalConstants;
import com.qa.utils.GlobalParams;
import com.qa.utils.VideoManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;

import java.io.File;
import java.io.FileOutputStream;

public class Hooks {
    GlobalParams params = new GlobalParams();

    @Before
    public void initialize(Scenario scenario) throws Exception {
        BasePage basePage = new BasePage();
        basePage.closeApp();
        basePage.launchApp();


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
    public void quit(Scenario scenario) throws Exception {
        byte[] video = new VideoManager().stopRecording(scenario.getName());
//        video = new VideoManager().compressVideoFurther(video);

        if (scenario.isFailed()) {
            byte[] screenshot = new DriverManager().getDriver().getScreenshotAs(OutputType.BYTES);

            //TODO reduce attachment size
            scenario.attach(screenshot, "image/png", scenario.getName());
//            scenario.attach(video, "video/mp4", "Video" + scenario.getName());

            attachVideoAsLink(video, scenario);
        }

//        System.out.println("##################### afterScenario");
//        String deviceName = new GlobalParams().getDeviceName();
//        scenario.attach("Device: " + deviceName, "text/plain", "Device Info");

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

    public void attachVideoAsLink(byte[] video, Scenario scenario) {
        String scenarioName = scenario.getName();
        try {
            // 1. Create a directory to store videos if it doesn't exist
            String dirPath = GlobalConstants.PROJECT_PATH + GlobalConstants.SEPARATOR + params.getPlatformName() + "_"
                    + params.getDeviceName() + File.separator + "Videos" + GlobalConstants.SEPARATOR + scenarioName + ".mp4";

//            String videoDir = "target/videos/";
//            Files.createDirectories(Paths.get(videoDir));
//
//            // 2. Create a unique filename for the video
//            String videoFileName = scenarioName.replaceAll("\\s+", "_") + "_" + System.currentTimeMillis() + ".mp4";
//            String videoPath = videoDir + videoFileName;

            // 3. Save the video to file
            try (FileOutputStream fos = new FileOutputStream(dirPath)) {
                fos.write(video);
            }

            // 4. Create a relative URL to the video file
            String videoUrl = "file:///" + dirPath;
//            String videoUrl = "https://drive.google.com/file/d/1ykSdGFxF8s0uxSZNJASmglqSQUZZcf-U/view?usp=drive_link";

            // 5. Generate HTML with link to the video
            String html = "<a href='" + videoUrl + "'>Click on Download button to view video</a>";

            // 6. Attach the HTML link to the report
            scenario.attach(html.getBytes(), "text/html", "Video link for " + scenarioName);
            scenario.attach(video, "video/mp4", "Video" + scenario.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}