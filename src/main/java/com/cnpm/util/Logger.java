package com.cnpm.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Logger {
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_LIGHT_YELLOW_BACKGROUND = "\u001B[103m";
    public static final String ANSI_RESET = "\u001B[0m"; // Reset màu

    public static void log(String message, Exception e) {
//        System.out.println(ANSI_LIGHT_YELLOW_BACKGROUND + ANSI_BLACK + message + ": " + e.getMessage() + ANSI_RESET);
        log.info(ANSI_LIGHT_YELLOW_BACKGROUND + ANSI_BLACK + "{}: {}" + ANSI_RESET, message, e.getMessage());
    }
    public static void log(String message) {
//        System.out.println(ANSI_LIGHT_YELLOW_BACKGROUND + ANSI_BLACK + message + ": " + e.getMessage() + ANSI_RESET);
        log.info(ANSI_LIGHT_YELLOW_BACKGROUND + ANSI_BLACK + "{}" + ANSI_RESET, message);
    }
}