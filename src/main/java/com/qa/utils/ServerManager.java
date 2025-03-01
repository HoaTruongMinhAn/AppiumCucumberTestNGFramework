package com.qa.utils;

import com.google.common.io.ByteStreams;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

public class ServerManager {
    private static ThreadLocal<AppiumDriverLocalService> server = new ThreadLocal<>();
    TestUtils utils = new TestUtils();

    public AppiumDriverLocalService getServer() {
        return server.get();
    }

    public void setServer(AppiumDriverLocalService server) {
        this.server.set(server);
    }

    public void startServer() {
//        utils.log().info("starting appium server");
//        AppiumDriverLocalService server = WindowsGetAppiumService();
//        server.start();
//        if (server == null || !server.isRunning()) {
//            utils.log().fatal("Appium server not started. ABORT!!!");
//            throw new AppiumServerHasNotBeenStartedLocallyException("Appium server not started. ABORT!!!");
//        }
//        server.clearOutPutStreams(); // -> Comment this if you want to see server logs in the console
//        this.server.set(server);
//        utils.log().info("Appium server started");

        //Start Appium server
        killAppiumServer();
        utils.log().info("starting appium server");
        setServer(getAppiumService());
        getServer().clearOutPutStreams();
        getServer().start();
        utils.log().info("Server started");
    }

    public void killAppiumServer() {
        String cmd = "";
        String cmd2 = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("window")) {
            cmd = "taskkill /F /IM node.exe";
            cmd2 = "taskkill /F /IM adb.exe";
        } else {
            cmd = "pkill node.exe";
            cmd2 = "pkill adb.exe";
        }

        try {
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();

            process = Runtime.getRuntime().exec(cmd2);
            process.waitFor();
            utils.log().info("Existing Appium server is already killed");
            utils.log().info("Is Appium server still alive? " + checkIfAppiumServerIsRunning(4723));
        } catch (Exception e) {
            utils.log().info(e.getMessage());
        }
    }

    public boolean checkIfAppiumServerIsRunning(int port) throws Exception {
        boolean isAppiumServerRunning = false;
        ServerSocket socket;
        try {
            socket = new ServerSocket(port);
            socket.close();
        } catch (IOException e) {
            utils.log().info("Port occupied !!!!!!!!!!!!!!!!!!!!!!");
            isAppiumServerRunning = true;
        } finally {
            socket = null;
        }
        return isAppiumServerRunning;
    }

    public AppiumDriverLocalService getAppiumServerDefault() {
        return AppiumDriverLocalService.buildDefaultService();
    }

    public AppiumDriverLocalService WindowsGetAppiumService() {
        GlobalParams params = new GlobalParams();
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingPort(4723)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withLogFile(new File(params.getPlatformName() + "_"
                        + params.getDeviceName() + File.separator + "Server.log")));
    }

    public AppiumDriverLocalService MacGetAppiumService() {
        GlobalParams params = new GlobalParams();
        HashMap<String, String> environment = new HashMap<String, String>();
        environment.put("PATH", "enter_your_path_here" + System.getenv("PATH"));
        environment.put("ANDROID_HOME", "enter_your_android_home_path");
        environment.put("JAVA_HOME", "enter_your_java_home_path");
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingDriverExecutable(new File("/usr/local/bin/node"))
                .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
                .usingAnyFreePort()
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withEnvironment(environment)
                .withLogFile(new File(params.getPlatformName() + "_"
                        + params.getDeviceName() + File.separator + "Server.log")));
    }

    public AppiumDriverLocalService getAppiumService() {
        HashMap<String, String> environment = new HashMap<String, String>();

        //Method can work without this option. It can be removed
        environment.put("PATH", "C:\\Program Files\\Common Files\\Oracle\\Java\\javapath;C:\\Program Files (x86)\\Common Files\\Oracle\\Java\\java8path;C:\\Program Files (x86)\\Common Files\\Oracle\\Java\\javapath;C:\\Program Files (x86)\\VMware\\VMware Workstation\\bin\\;C:\\Program Files\\Microsoft MPI\\Bin\\;C:\\WINDOWS\\system32;C:\\WINDOWS;C:\\WINDOWS\\System32\\Wbem;C:\\WINDOWS\\System32\\WindowsPowerShell\\v1.0\\;C:\\WINDOWS\\System32\\OpenSSH\\;C:\\Program Files\\Git\\cmd;C:\\Program Files\\Java\\jdk-17\\bin;C:\\Users\\minhanhoa.truong\\Automation\\01 - Software\\Report\\allure-2.32.0\\allure-2.32.0\\bin;C:\\Users\\minhanhoa.truong\\Automation\\01 - Software\\cmder_mini\\bin;C:\\Program Files\\dotnet\\;C:\\Program Files (x86)\\Microsoft SQL Server\\160\\Tools\\Binn\\;C:\\Program Files\\Microsoft SQL Server\\160\\Tools\\Binn\\;C:\\Program Files\\Microsoft SQL Server\\Client SDK\\ODBC\\170\\Tools\\Binn\\;C:\\Program Files\\Microsoft SQL Server\\160\\DTS\\Binn\\;C:\\Program Files (x86)\\Microsoft SQL Server\\160\\DTS\\Binn\\;C:\\Program Files\\Azure Data Studio\\bin;C:\\Users\\minhanhoa.truong\\Automation\\01 - Software\\Maven\\apache-maven-3.9.9-bin\\apache-maven-3.9.9\\bin;C:\\Users\\minhanhoa.truong\\Automation\\01 - Software\\Gradle\\gradle-7.3.3-bin\\gradle-7.3.3\\bin;C:\\Program Files\\Amazon\\AWSCLIV2\\;C:\\Program Files\\Docker\\Docker\\resources\\bin;C:\\Program Files\\nodejs\\;C:\\Program Files\\Cloudflare\\Cloudflare WARP\\;C:\\Users\\minhanhoa.truong\\AppData\\Local\\Android\\Sdk\\platform-tools;C:\\Users\\minhanhoa.truong\\AppData\\Local\\Android\\Sdk\\cmdline-tools;C:\\Users\\minhanhoa.truong\\AppData\\Local\\Microsoft\\WindowsApps;C:\\Program Files\\JetBrains\\IntelliJ IDEA Community Edition 2024.2.3\\bin;C:\\Program Files\\JetBrains\\IntelliJ IDEA 2024.1\\bin;C:\\Users\\minhanhoa.truong\\AppData\\Local\\JetBrains\\Toolbox\\scripts;;C:\\Program Files\\Azure Data Studio\\bin;C:\\Users\\minhanhoa.truong\\AppData\\Roaming\\npm" + System.getenv("PATH"));


        //Method can work without this option. It can be removed
        environment.put("ANDROID_HOME", "C:\\Users\\minhanhoa.truong\\AppData\\Local\\Android\\Sdk");

        //All options below are optional. They can be used or removed
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe"))
                .withAppiumJS(new File("C:\\Users\\minhanhoa.truong\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
                .usingPort(4723)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withEnvironment(environment)
                .withLogOutput(ByteStreams.nullOutputStream())
                .withLogFile(new File("ServerLogs/server.log"))
        );
    }
}
