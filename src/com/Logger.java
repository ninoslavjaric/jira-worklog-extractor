package com;

public class Logger {
    private static final String ANSI_RESET = "\u001B[0m";

    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    private static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

//        System.out.println(ANSI_GREEN_BACKGROUND + "This text has a green background but default text!" + ANSI_RESET);
//        System.out.println(ANSI_RED + "This text has red text but a default background!" + ANSI_RESET);
//        System.out.println(ANSI_GREEN_BACKGROUND + ANSI_RED + "This text has a green background and red text!" + ANSI_RESET);

    public static void info(String info) {
        info(info, true);
    }

    public static void info(String info, boolean br) {
        print(ANSI_BLUE, info, br);
    }

    public static void warn(String warning) {
        warn(warning, true);
    }

    private static void warn(String warning, boolean br) {
        print(ANSI_YELLOW, warning, br);
    }

    public static void success(String warning) {
        success(warning, true);
    }

    private static void success(String warning, boolean br) {
        print(ANSI_GREEN, warning, br);
    }

    private static void print(String color, String string, boolean br) {
        System.out.print(String.format("%s%s%s%s", color, string, ANSI_RESET, (br ? "\n" : "")));
    }
}
