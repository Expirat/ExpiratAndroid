package com.expirate.expirat.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.expirate.expirat.R;


public class EmptyExpireView extends LinearLayout {
    public EmptyExpireView(Context context) {
        super(context);
    }

    public EmptyExpireView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_empty_expire_list, this);
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
    }
}
