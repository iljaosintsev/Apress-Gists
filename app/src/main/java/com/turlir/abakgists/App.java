package com.turlir.abakgists;


import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.turlir.abakgists.di.AppComponent;
import com.turlir.abakgists.di.AppModule;
import com.turlir.abakgists.di.DaggerAppComponent;
import com.turlir.abakgists.di.PresenterModule;

public class App extends Application {

    private static AppComponent sComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        sComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .presenterModule(new PresenterModule())
                .build();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectNetwork()
                    .penaltyLog()
                    .penaltyDeathOnNetwork()
                    .build());
        }
    }

    public static AppComponent getComponent() {
        return sComponent;
    }

}
