package com.expirate.expirat.ui.home;


import com.google.auto.value.AutoValue;

import com.expirate.expirat.services.response.TypesItem;
import com.expirate.expirat.utils.AutoGson;

import java.util.List;

@AutoValue @AutoGson
public abstract class TypesInfo {
    public abstract List<TypesItem> types();

    public static TypesInfo create(List<TypesItem> types) {
        return new AutoValue_TypesInfo(types);
    }
}
