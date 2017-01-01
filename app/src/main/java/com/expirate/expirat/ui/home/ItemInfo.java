package com.expirate.expirat.ui.home;


import com.google.auto.value.AutoValue;

import com.expirate.expirat.utils.AutoGson;

@AutoValue @AutoGson
public abstract class ItemInfo {
    public abstract int countItem();

    public abstract int countItemExpired();

    public static ItemInfo create(int countItem, int countItemExpired) {
        return new AutoValue_ItemInfo(countItem, countItemExpired);
    }
}
