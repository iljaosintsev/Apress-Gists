package com.turlir.abakgists.base;


import android.app.Application;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.turlir.abakgists.di.AppComponent;
import com.turlir.abakgists.di.AppModule;
import com.turlir.abakgists.di.DaggerAppComponent;

import timber.log.Timber;

public class App extends Application {

    private static AppComponent sComponent;

    public static AppComponent getComponent() {
        return sComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        StrictMode.setThreadPolicy(
                new StrictMode.ThreadPolicy.Builder()
                        .detectNetwork()
                        .penaltyLog()
                        .penaltyDeathOnNetwork()
                        .build()
        );

        tooling();
    }

    protected void tooling() {
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }
        Timber.plant(new Timber.DebugTree());
        Stetho.initializeWithDefaults(this);
    }

}
