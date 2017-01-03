package com.expirate.expirat.utils;


public final class DateUtils {
    private DateUtils() {}

    public static int dayDiff(long unixTime, long compareTime) {
        long diff =     unixTime - compareTime;
        long dayDiff = diff / (60 * 60 * 24);
        return (int) dayDiff;
    }

    public static int getYear(String fullDate) {
        String[] splitDate = splitFullDateString(fullDate);
        return Integer.parseInt(splitDate[2]);
    }

    public static int getMonth(String fullDate) {
        String[] splitDate = splitFullDateString(fullDate);
        return Integer.parseInt(splitDate[1]) - 1;
    }

    public static int getDay(String fullDate) {
        String[] splitDate = splitFullDateString(fullDate);
        return Integer.parseInt(splitDate[0]);
    }

    private static String[] splitFullDateString(String fullDate) {
        return fullDate.split("-");
    }
}
