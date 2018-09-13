package com.turlir.abakgists.di;


import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.turlir.abakgists.AppDatabase;
import com.turlir.abakgists.BuildConfig;
import com.turlir.abakgists.gistsloader.SourceListing;
import com.turlir.abakgists.api.ApiClient;
import com.turlir.abakgists.api.LogInterceptor;
import com.turlir.abakgists.api.Repository;

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
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .cache(OkHttp3Downloader.createDefaultCache(cnt))
                .build();
        OkHttp3Downloader network = new OkHttp3Downloader(client);
        Picasso pic = new Picasso.Builder(cnt)
                .indicatorsEnabled(BuildConfig.DEBUG)
                .loggingEnabled(false)
                .downloader(network)
                .build();
        Picasso.setSingletonInstance(pic);
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
    public Repository provideRepository(ApiClient client, AppDatabase db) {
        return new Repository(client, db.gistDao());
    }

    @Provides
    public SourceListing provideSourceListing(Repository repo) {
        return new SourceListing(repo);
    }

}
