package com.turlir.abakgists.di;


import android.content.Context;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.turlir.abakgists.network.ApiClient;
import com.turlir.abakgists.network.LogInterceptor;
import com.turlir.abakgists.network.Repository;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class AppModule {

    private final Context mContext;

    public AppModule(Context cnt) {
        mContext = cnt;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new LogInterceptor())
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    public ApiClient provideApiClient(OkHttpClient okhttp) {
        return new ApiClient(okhttp);
    }

    @Provides
    @Singleton
    public Repository provideRepository(ApiClient client, StorIOSQLite base) {
        return new Repository(client, base);
    }

}
