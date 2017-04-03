package com.turlir.abakgists.di;


import android.content.Context;

import com.turlir.abakgists.network.ApiClient;
import com.turlir.abakgists.network.LogInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class AppModule {

    private final Context mContext;

    public AppModule(Context cnt) {
        this.mContext = cnt;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new LogInterceptor())
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    ApiClient provideApiClient(OkHttpClient okhttp) {
        return new ApiClient(okhttp);
    }

}
