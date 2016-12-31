package com.expirate.expirat.utils;


import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;

import com.expirate.expirat.R;

public final class StringStyleUtils {
    private StringStyleUtils(){}

    public static Spannable dayLeftTextAppearance(Context context, String normalText) {
        TextAppearanceSpan dayLeftSpan = new TextAppearanceSpan(context,
                R.style.DaysLeftTextAppearance);
        TextAppearanceSpan labelDayLeftSpan = new TextAppearanceSpan(context,
                R.style.LabelDaysLeftTextAppearance);

        String[] temp = normalText.split("\n");

        Spannable spannable = new SpannableString(normalText);

        if (temp.length > 2) {
            spannable.setSpan(dayLeftSpan, 0, temp[0].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(labelDayLeftSpan, temp[0].length() + 1, normalText.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spannable;
    }

    public static Spannable dateTextAppearance(Context context, String normalText) {
        TextAppearanceSpan labelSpan = new TextAppearanceSpan(
                context, R.style.LabelTextAppearance
        );

        TextAppearanceSpan dateSpan = new TextAppearanceSpan(
                context, R.style.DateTextAppearance
        );

        String[] temp = normalText.split("\n");

        Spannable spannable = new SpannableString(normalText);
        spannable.setSpan(labelSpan, 0, temp[0].length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(dateSpan, temp[0].length() + 1, normalText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }
}
