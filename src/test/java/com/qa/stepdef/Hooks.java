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

    /*public void attachVideoAsLink(byte[] video, Scenario scenario) {
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
//            try (FileOutputStream fos = new FileOutputStream(filePath)) {
//                fos.write(video);
//            }

            // 4. Use a relative path that will work after publication
//            String videoUrl = "../" + params.getPlatformName() + "_" + params.getDeviceName() + "/" + "Videos" + "/" + videoFileName;
            String videoUrl = "./" + reportDirFile + "/" + videoFileName;
            utils.log().info("videoUrl: " + videoUrl);

            // 5. Generate HTML with a proper video element
            //Option 1
            String html = "<video width='320' height='240' controls autoplay muted>" +
                    "<source src='" + videoUrl + "' type='video/mp4'>" +
                    "Your browser does not support the video tag." +
                    "</video>";
            html = html.replace("\\", "/");
            utils.log().info("html: " + html);

            // 6. Attach the HTML to the report
            scenario.attach(html.getBytes(), "text/html", "Video for " + scenarioName);


            //Option 2
            String html2 = "<video width='320' height='240' controls autoplay muted>" +
                    "<source src='C:/Users/minhanhoa.truong/Automation_Appium/06%20-%20Cucumber%20TestNG/AppiumCucumberTestNGFramework/target/cucumber-reports/cucumber-html-reports/Android_SamsungA55/Videos/ValidateProductInfoOnProductsPage_Android_SamsungA55.mp4' type='video/mp4'>" +
                    "Your browser does not support the video tag." +
                    "</video>";
            html2 = html2.replace("\\", "/");
            utils.log().info("html: " + html2);

            // 6. Attach the HTML to the report
            scenario.attach(html2.getBytes(), "text/html", "2222222222 Video for " + scenarioName);

            //Option 3
            String html3 = "<a href=\"" + videoUrl + "\" target=\"_blank\">Video here kkkkkkk</a> ";
            html3 = html3.replace("\\", "/");
            utils.log().info("html: " + html3);

            // 6. Attach the HTML to the report
            scenario.attach(html3.getBytes(), "text/html", "33333333333 Video for " + scenarioName);

            //Option 4
            String html4 = "<video width='320' height='240' controls autoplay muted>" +
                    "<source src='https://drive.google.com/file/d/1dfDBRv9f8pqDf96bSpSgCURSzR8TWK9q/view?usp=sharing' type='video/mp4'>" +
                    "Your browser does not support the video tag." +
                    "</video>";
            html4 = html4.replace("\\", "/");
            utils.log().info("html: " + html4);

            // 6. Attach the HTML to the report
            scenario.attach(html4.getBytes(), "text/html", "4444444444444444 Video for " + scenarioName);

            //Option 5
            String html5 = "<video width='320' height='240' controls autoplay muted>" +
                    "<source src='https://drive.google.com/file/d/1dfDBRv9f8pqDf96bSpSgCURSzR8TWK9q/preview?autoplay=1' type='video/mp4'>" +
                    "Your browser does not support the video tag." +
                    "</video>";
            html5 = html5.replace("\\", "/");
            utils.log().info("html: " + html5);

            // 6. Attach the HTML to the report
            scenario.attach(html5.getBytes(), "text/html", "55555555555 Video for " + scenarioName);

            //Option 6
            String html6 = "<div style='text-align:center; padding:20px;'>" +
                    "<a href='https://drive.google.com/file/d/1dfDBRv9f8pqDf96bSpSgCURSzR8TWK9q' target='_blank' rel='noopener noreferrer' " +
                    "onclick='window.open(this.href, \"_blank\"); return false;' " +
                    "style='display:inline-block; padding:12px 24px; background-color:#4285F4; " +
                    "color:white; text-decoration:none; font-weight:bold; border-radius:4px;'>" +
                    "Click to View Video" +
                    "</a>" +
                    "<p style='margin-top:10px;'>Video will open in a new tab</p>" +
                    "</div>";
            html6 = html6.replace("\\", "/");
            utils.log().info("html: " + html6);

            // 6. Attach the HTML to the report
            scenario.attach(html6.getBytes(), "text/html", "666666666 Video for " + scenarioName);

            /*//Option 7 JS
            String containerId = "video-container-" + System.currentTimeMillis();
            String html7 = "<div id='" + containerId + "' style='width:640px; height:360px; background:#000; display:flex; align-items:center; justify-content:center;'>" +
                    "<p style='color:white;'>Loading video player...</p>" +
                    "</div>" +
                    "<script>" +
                    "  (function() {" +
                    "    // Create video element dynamically" +
                    "    var container = document.getElementById('" + containerId + "');" +
                    "    if (!container) return;" +
                    "    " +
                    "    // Clear the container" +
                    "    container.innerHTML = '';" +
                    "    " +
                    "    // Create video element" +
                    "    var video = document.createElement('video');" +
                    "    video.width = 640;" +
                    "    video.height = 360;" +
                    "    video.controls = true;" +
                    "    video.autoplay = true;" +
                    "    video.muted = true;" +
                    "    video.style.maxWidth = '100%';" +
                    "    " +
                    "    // Create source element" +
                    "    var source = document.createElement('source');" +
                    "    source.src = 'https://drive.google.com/file/d/1dfDBRv9f8pqDf96bSpSgCURSzR8TWK9q';" +
                    "    source.type = 'video/mp4';" +
                    "    " +
                    "    // Append elements" +
                    "    video.appendChild(source);" +
                    "    container.appendChild(video);" +
                    "    " +
                    "    // Add fallback text" +
                    "    var fallback = document.createElement('p');" +
                    "    fallback.textContent = 'Your browser does not support the video tag.';" +
                    "    video.appendChild(fallback);" +
                    "    " +
                    "    // Try to play the video" +
                    "    video.addEventListener('loadeddata', function() {" +
                    "      try {" +
                    "        var playPromise = video.play();" +
                    "        if (playPromise !== undefined) {" +
                    "          playPromise.catch(function(error) {" +
                    "            console.log('Autoplay was prevented');" +
                    "          });" +
                    "        }" +
                    "      } catch(e) {}" +
                    "    });" +
                    "  })();" +
                    "</script>";
            html7 = html7.replace("\\", "/");
            utils.log().info("html: " + html7);

            // 6. Attach the HTML to the report
            scenario.attach(html7.getBytes(), "text/html", "77777777777 Video for " + scenarioName);*/

            //Option 8 JS
            /*String containerId = "video-container-" + System.currentTimeMillis();
            String html7 = "<div id='" + containerId + "' style='width:640px; height:360px; background:#000; display:flex; align-items:center; justify-content:center;'>" +
                    "<p style='color:white;'>Loading video player...</p>" +
                    "</div>" +
                    "<script>" +
                    "  (function() {" +
                    "    // Create video element dynamically" +
                    "    var container = document.getElementById('" + containerId + "');" +
                    "    if (!container) return;" +
                    "    " +
                    "    // Clear the container" +
                    "    container.innerHTML = '';" +
                    "    " +
                    "    // Create video element" +
                    "    var video = document.createElement('video');" +
                    "    video.width = 640;" +
                    "    video.height = 360;" +
                    "    video.controls = true;" +
                    "    video.autoplay = true;" +
                    "    video.muted = true;" +
                    "    video.style.maxWidth = '100%';" +
                    "    " +
                    "    // Create source element" +
                    "    var source = document.createElement('source');" +
                    "    source.src = 'https://drive.google.com/uc?export=download&id=1dfDBRv9f8pqDf96bSpSgCURSzR8TWK9q';" +
                    "    source.type = 'video/mp4';" +
                    "    " +
                    "    // Append elements" +
                    "    video.appendChild(source);" +
                    "    container.appendChild(video);" +
                    "    " +
                    "    // Add fallback text" +
                    "    var fallback = document.createElement('p');" +
                    "    fallback.textContent = 'Your browser does not support the video tag.';" +
                    "    video.appendChild(fallback);" +
                    "    " +
                    "    // Try to play the video" +
                    "    video.addEventListener('loadeddata', function() {" +
                    "      try {" +
                    "        var playPromise = video.play();" +
                    "        if (playPromise !== undefined) {" +
                    "          playPromise.catch(function(error) {" +
                    "            console.log('Autoplay was prevented');" +
                    "          });" +
                    "        }" +
                    "      } catch(e) {}" +
                    "    });" +
                    "  })();" +
                    "</script>";
            html7 = html7.replace("\\", "/");
            utils.log().info("html: " + html7);

            // 6. Attach the HTML to the report
            scenario.attach(html7.getBytes(), "text/html", "77777777777 Video for " + scenarioName);*/

            //Option 9 JS
            /*String containerId = "video-container-" + System.currentTimeMillis();
            String html7 = """
                    <div id='video-container-1741344235814' style='width:640px; height:360px; background:#000; display:flex; align-items:center; justify-content:center;'>
                      <p style='color:white;'>Loading video player...</p>
                    </div>
                    <script>
                      (function() {
                        // Create video element dynamically
                        var container = document.getElementById('video-container-1741344235814');
                        if (!container) return;
                       \s
                        // Clear the container
                        container.innerHTML = '';
                       \s
                        // Create video element
                        var video = document.createElement('video');
                        video.width = 640;
                        video.height = 360;
                        video.controls = true;
                        video.autoplay = true;
                        video.muted = true;
                        video.style.maxWidth = '100%';
                       \s
                        // Create source element
                        var source = document.createElement('source');
                        source.src = 'https://drive.google.com/uc?export=download&id=1dfDBRv9f8pqDf96bSpSgCURSzR8TWK9q';
                        source.type = 'video/mp4';
                       \s
                        // Append elements
                        video.appendChild(source);
                        container.appendChild(video);
                       \s
                        // Add fallback text
                        var fallback = document.createElement('p');
                        fallback.textContent = 'Your browser does not support the video tag.';
                        video.appendChild(fallback);
                       \s
                        // Try to play the video
                        video.addEventListener('loadeddata', function() {
                          try {
                            var playPromise = video.play();
                            if (playPromise !== undefined) {
                              playPromise.catch(function(error) {
                                console.log('Autoplay was prevented');
                              });
                            }
                          } catch(e) {
                            console.error('Error playing video:', e);
                          }
                        });
                      })();
                    </script>
                    """;
//            html7 = html7.replace("\\", "/");
            utils.log().info("html: " + html7);

            // 6. Attach the HTML to the report
            scenario.attach(html7.getBytes(), "text/html", "77777777777 Video for " + scenarioName);*/

            //Option 10 JS
            String containerId = "video-container-" + System.currentTimeMillis();
            String html7 = """
                    <div id='video-container-1741344235814' style='width:640px; height:360px; background:#000; display:flex; align-items:center; justify-content:center;'>
                      <p style='color:white;'>Loading video player...</p>
                    </div>
                    <script>
                      (function() {
                        // Create video element dynamically
                        var container = document.getElementById('video-container-1741344235814');
                        if (!container) return;
                       \s
                        // Clear the container
                        container.innerHTML = '';
                       \s
                        // Create video element
                        var video = document.createElement('video');
                        video.width = 640;
                        video.height = 360;
                        video.controls = true;
                        video.autoplay = true;
                        video.muted = true;
                        video.style.maxWidth = '100%';
                       \s
                        // Create source element
                        var source = document.createElement('source');
                        source.src = './Android_SamsungA55/Videos/ValidateProductInfoOnProductsPage_Android_SamsungA55.mp4';
                        source.type = 'video/mp4';
                       \s
                        // Append elements
                        video.appendChild(source);
                        container.appendChild(video);
                       \s
                        // Add fallback text
                        var fallback = document.createElement('p');
                        fallback.textContent = 'Your browser does not support the video tag.';
                        video.appendChild(fallback);
                       \s
                        // Try to play the video
                        video.addEventListener('loadeddata', function() {
                          try {
                            var playPromise = video.play();
                            if (playPromise !== undefined) {
                              playPromise.catch(function(error) {
                                console.log('Autoplay was prevented');
                              });
                            }
                          } catch(e) {
                            console.error('Error playing video:', e);
                          }
                        });
                      })();
                    </script>
                    """;
//            html7 = html7.replace("\\", "/");
            utils.log().info("html: " + html7);

            // 6. Attach the HTML to the report
            scenario.attach(html7.getBytes(), "text/html", "77777777777 Video for " + scenarioName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        utils.log().info("Hooks: attachVideoAsLink completed.");
    }

}