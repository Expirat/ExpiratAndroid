package com.expirate.expirat;


import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application providerApplication() {
        return application;
    }

    @Provides
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    GoogleAnalytics provideGoogleAnalytics(Context context) {
        return GoogleAnalytics.getInstance(context);
    }

    @Provides
    Tracker provideTracker(GoogleAnalytics googleAnalytics) {
        return googleAnalytics.newTracker(R.xml.global_tracker);
    }

}
