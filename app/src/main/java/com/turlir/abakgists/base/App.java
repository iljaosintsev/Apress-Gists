package com.turlir.abakgists.base;


import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.turlir.abakgists.di.AppComponent;
import com.turlir.abakgists.di.AppModule;
import com.turlir.abakgists.di.DaggerAppComponent;

import timber.log.Timber;

public class App extends Application {

    private static AppComponent sComponent;

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

        initStetho();

        Timber.plant(new Timber.DebugTree());
    }

    private void initStetho() {
        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);
    }

    public static AppComponent getComponent() {
        return sComponent;
    }

}
