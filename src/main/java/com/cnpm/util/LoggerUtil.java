package com.cnpm.util;

public class LoggerUtil {
//    màu đen
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_LIGHT_YELLOW_BACKGROUND = "\u001B[103m";
    public static final String ANSI_RESET = "\u001B[0m"; // Reset màu

    public static void logError(String message, Exception e) {
        System.out.println(ANSI_LIGHT_YELLOW_BACKGROUND + ANSI_BLACK + message + ": " + e.getMessage() + ANSI_RESET);
    }
}