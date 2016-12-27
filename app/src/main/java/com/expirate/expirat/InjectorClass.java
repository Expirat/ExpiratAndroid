package com.expirate.expirat;


import android.app.Application;

public enum InjectorClass {
    INSTANCE;

    private ApplicationGraph applicationGraph;

    public void initApplicationComponent(Application application) {
        applicationGraph = DaggerApplicationComponent.create();
    }

    public ApplicationGraph getApplicationGraph() {
        return applicationGraph;
    }
}
