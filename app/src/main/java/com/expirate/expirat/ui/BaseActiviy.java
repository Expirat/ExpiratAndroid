package com.expirate.expirat.ui;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class BaseActiviy extends AppCompatActivity {

    public void setupToolbar(Toolbar toolbar, @Nullable String title, boolean allowUpNavigation) {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() == null) {
            throw new IllegalStateException("Activity must implement toolbar");
        }

        if (title != null) {
            getSupportActionBar().setTitle(title);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(allowUpNavigation);
    }
}
