package com.turlir.abakgists.base;


import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

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

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder()
                            .detectNetwork()
                            .penaltyLog()
                            .penaltyDeathOnNetwork()
                            .build()
            );
        }

        Timber.plant(new Timber.DebugTree());
    }

}
