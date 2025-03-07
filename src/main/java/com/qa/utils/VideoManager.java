package com.qa.utils;

import io.appium.java_client.screenrecording.CanRecordScreen;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileOutputStream;

public class VideoManager {
    TestUtils utils = new TestUtils();

    public void startRecording() {
        ((CanRecordScreen) new DriverManager().getDriver()).startRecordingScreen();
    }

    /*public byte[] stopRecording(String scenarioName) throws IOException {
        GlobalParams params = new GlobalParams();
        String media = ((CanRecordScreen) new DriverManager().getDriver()).stopRecordingScreen();
        String dirPath = params.getPlatformName() + "_"
                + params.getDeviceName() + File.separator + "Videos";

        File videoDir = new File(dirPath);

        synchronized (videoDir) {
            if (!videoDir.exists()) {
                videoDir.mkdirs();
            }
        }
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(videoDir + File.separator + scenarioName + ".mp4");
            stream.write(Base64.decodeBase64(media));
            stream.close();
            utils.log().info("video path: " + videoDir + File.separator + scenarioName + ".mp4");
        } catch (Exception e) {
            utils.log().error("error during video capture" + e.toString());
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return Base64.decodeBase64(media);
    }*/

    public byte[] stopRecording(String scenarioName) throws Exception {
        GlobalParams params = new GlobalParams();
        String media = ((CanRecordScreen) new DriverManager().getDriver()).stopRecordingScreen();
        //Store on src
//        String dirPath = params.getPlatformName() + "_" + params.getDeviceName() + File.separator + "Videos";

        //Store on target
//        String dirPath = "target" + GlobalConstants.SEPARATOR + "cucumber-reports" + GlobalConstants.SEPARATOR + "cucumber-html-reports" + GlobalConstants.SEPARATOR + params.getPlatformName() + "_" + params.getDeviceName() + GlobalConstants.SEPARATOR + "Videos";
        String dirPath = "target" + "/" + "cucumber-reports" + "/" + "cucumber-html-reports" + "/" + params.getPlatformName() + "_" + params.getDeviceName() + "/" + "Videos";

        File videoDir = new File(dirPath);

        synchronized (videoDir) {
            if (!videoDir.exists()) {
                videoDir.mkdirs();
            }
        }
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(videoDir + "/" + scenarioName + "_" + params.getPlatformName() + "_" + params.getDeviceName() + ".mp4");
            stream.write(Base64.decodeBase64(media));
            stream.close();
            utils.log().info("video path: " + videoDir + "/" + scenarioName + "_" + params.getPlatformName() + "_" + params.getDeviceName() + ".mp4");
        } catch (Exception e) {
            utils.log().error("error during video capture" + e.toString());
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        utils.log().info("VideoManager: video capture completed.");
        return Base64.decodeBase64(media);
    }


}
