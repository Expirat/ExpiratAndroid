package com.expirate.expirat;


import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.evernote.android.job.JobManager;
import com.expirate.expirat.scheduler.ExpiredJobCreator;
import io.fabric.sdk.android.Fabric;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        InjectorClass.INSTANCE.initApplicationComponent(this);

        JobManager.create(this).addJobCreator(new ExpiredJobCreator());

    }
}
