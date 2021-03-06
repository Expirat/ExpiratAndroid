package com.expirate.expirat.services.response;


import com.google.auto.value.AutoValue;

import android.support.annotation.Nullable;

import com.expirate.expirat.utils.AutoGson;

@AutoValue @AutoGson
public abstract class GroceriesItem {
    @Nullable
    public abstract Long id();

    public abstract String name();

    @Nullable
    public abstract String barcode();

    @Nullable
    public abstract Long buyDate();

    public abstract long expiredDate();

    @Nullable
    public abstract Long createdDate();

    public abstract long modifiedDate();

    @Nullable
    public abstract Integer type();

    @Nullable
    public abstract String typeName();

    public static Builder builder() {
        return new AutoValue_GroceriesItem.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(Long id);

        public abstract Builder setName(String name);

        public abstract Builder setBarcode(String barcode);

        public abstract Builder setBuyDate(Long buyDate);

        public abstract Builder setExpiredDate(long expiredDate);

        public abstract Builder setCreatedDate(Long createdDate);

        public abstract Builder setModifiedDate(long modifiedDate);

        public abstract Builder setType(Integer type);

        public abstract Builder setTypeName(String typeName);

        public abstract GroceriesItem build();
    }
}
