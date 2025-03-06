package com.qa.utils;

import java.io.File;

public class GlobalConstants {
    public static final String PROJECT_PATH = System.getProperty("user.dir");
    // Wait Infor
    public static final long VERY_SHORT_TIMEOUT = 1;
    public static final long SHORT_TIMEOUT = 3;
    public static final long MEDIUM_TIMEOUT = 10;
    public static final long LONG_TIMEOUT = 30;
    public static final String SEPARATOR = File.separator;
    private static GlobalParams params = new GlobalParams();
//    public static final String VIDEO_PATH = "target" + SEPARATOR + "cucumber-reports" + SEPARATOR + "cucumber-html-reports" + SEPARATOR + params.getPlatformName() + "_" + params.getDeviceName() + SEPARATOR + "Videos";
}
