package com.expirate.expirat.utils;


public final class DateUtils {
    private DateUtils() {}

    public static int dayDiff(long unixTime, long compareTime) {
        long diff =     unixTime - compareTime;
        long dayDiff = diff / (60 * 60 * 24);
        return (int) dayDiff;
    }
}
