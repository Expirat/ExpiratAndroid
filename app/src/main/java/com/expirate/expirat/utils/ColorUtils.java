package com.expirate.expirat.utils;


import android.content.Context;

import com.expirate.expirat.R;

public final class ColorUtils {
    private ColorUtils() {
    }

    public static int colorById(Context context, int id) {
        int positionId = id - 1;

        int[] groupsColor = context.getResources().getIntArray(R.array.group_colors);
        return groupsColor[ positionId % groupsColor.length];
    }
}
