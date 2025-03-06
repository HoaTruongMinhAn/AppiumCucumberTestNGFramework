package com.qa.stepdef;

import com.qa.pages.BasePage;
import com.qa.utils.DriverManager;
import com.qa.utils.GlobalParams;
import com.qa.utils.TestUtils;
import com.qa.utils.VideoManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;

import java.io.File;
import java.io.FileOutputStream;

public class Hooks {
    GlobalParams params = new GlobalParams();
    TestUtils utils = new TestUtils();

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

/*    public void attachVideoAsLink(byte[] video, Scenario scenario) {
        String scenarioName = scenario.getName();
        try {
            // 1. Create a directory to store videos if it doesn't exist
            String dirPath = GlobalConstants.PROJECT_PATH + GlobalConstants.SEPARATOR + params.getPlatformName() + "_" + params.getDeviceName() + File.separator + "Videos" + GlobalConstants.SEPARATOR + scenarioName + ".mp4";

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

            //This option causes error JSON exceed max length
//            scenario.attach(video, "video/mp4", "Video" + scenario.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void attachVideoAsLink(byte[] video, Scenario scenario) {
        String scenarioName = scenario.getName();
        try {
            // 1. Create a relative directory for videos within the report structure
            File reportDirFile = new File(params.getPlatformName() + "_" + params.getDeviceName() + "/" + "Videos");
            utils.log().info("reportDirFile: " + reportDirFile);
            if (!reportDirFile.exists()) {
                reportDirFile.mkdirs();
            }

            // 2. Create a filename for the video
            String videoFileName = scenarioName + "_" + params.getPlatformName() + "_" + params.getDeviceName() + ".mp4";
            String filePath = reportDirFile.getAbsolutePath() + "/" + videoFileName;
            utils.log().info("videoFileName: " + videoFileName);
            utils.log().info("filePath: " + filePath);

            // 3. Save the video to file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(video);
            }

            // 4. Use a relative path that will work after publication
//            String videoUrl = "../" + params.getPlatformName() + "_" + params.getDeviceName() + "/" + "Videos" + "/" + videoFileName;
            String videoUrl = reportDirFile + "/" + videoFileName;
            utils.log().info("videoUrl: " + videoUrl);

            // 5. Generate HTML with a proper video element
            String html = "<video width='320' height='240' controls>" +
                    "<source src='" + videoUrl + "' type='video/mp4'>" +
                    "Your browser does not support the video tag." +
                    "</video>";
            html = html.replace("\\", "/");
            utils.log().info("html: " + html);

            // 6. Attach the HTML to the report
            scenario.attach(html.getBytes(), "text/html", "Video for " + scenarioName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}