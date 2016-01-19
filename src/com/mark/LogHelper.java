package com.mark;

/**
 * Helper class to log messages to the console.
 */
public class LogHelper {

    // Logs a basic message to the log
    public static void log(String message) {
        System.out.println(message);
    }

    // Logs an error message to the log
    public static void logError(String errorMessage) {
        System.out.println(errorMessage);
    }

    // Logs an exception to the log
    public static void logException(Exception e) {
        System.out.println(e);
        e.printStackTrace();
    }

    // Logs an empty line to the console
    public static void logNewLine() {
        System.out.println();
    }
}
