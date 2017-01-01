package com.expirate.expirat.services.response;


import com.google.auto.value.AutoValue;

import com.expirate.expirat.utils.AutoGson;

@AutoValue @AutoGson
public abstract class TypesItem {

    public abstract long id();

    public abstract String typesName();

    public static TypesItem create(long id, String typesName) {
        return new AutoValue_TypesItem(id, typesName);
    }
}
