package com.expirate.expirat.ui;


import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class BaseHolder extends RecyclerView.ViewHolder {

    public BaseHolder(@LayoutRes int resId, ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
    }
}
