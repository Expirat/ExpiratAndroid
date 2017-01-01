package com.expirate.expirat.services.response;


import com.google.auto.value.AutoValue;

import com.expirate.expirat.utils.AutoGson;

import java.util.List;

@AutoValue @AutoGson
public abstract class Dashboards {

    public abstract int countAllItems();

    public abstract int countAllItemsThatExpired();

    public abstract List<TypesItem> types();

    public static Dashboards create(int countAllItems, int countAllItemsThatExpired, List<TypesItem> types) {
        return new AutoValue_Dashboards(countAllItems, countAllItemsThatExpired, types);
    }
}
