package com.expirate.expirat.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class StringUtils {
    private StringUtils() {}

    public static String getStringDate(long date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy",
                Locale.getDefault());
        return simpleDateFormat.format(new Date(date * 1000));
    }

    public static String setDate(int year, int monthOfYear, int dayOfMonth) {
        return String.format(Locale.getDefault(), "%d-%d-%d", dayOfMonth, monthOfYear+1, year);
    }

    public static String getToday() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int monthOfYear = now.get(Calendar.MONTH);
        int dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
        return String.format(Locale.getDefault(), "%d-%d-%d", dayOfMonth, monthOfYear+1, year);
    }

    public static String getFirstCharacterEachWord(String fullText) {
        StringBuilder sb = new StringBuilder();
        for (String text : fullText.split(" ")) {
            sb.append(text.charAt(0));
        }
        return sb.toString();
    }
}
