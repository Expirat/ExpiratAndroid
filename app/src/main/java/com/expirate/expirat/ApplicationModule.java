package com.expirate.expirat;


import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application providerApplication() {
        return application;
    }

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    GoogleAnalytics provideGoogleAnalytics(Context context) {
        return GoogleAnalytics.getInstance(context);
    }

    @Provides
    @Singleton
    Tracker provideTracker(GoogleAnalytics googleAnalytics) {
        return googleAnalytics.newTracker(R.xml.global_tracker);
    }

}
