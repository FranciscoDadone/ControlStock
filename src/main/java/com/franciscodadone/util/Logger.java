package com.franciscodadone.util;

public class Logger {

    public static void log(String str) {
        System.out.println("[" + new FDate() + "] " + str);
    }

}
