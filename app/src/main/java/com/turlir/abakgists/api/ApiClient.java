package com.turlir.abakgists.api;


import com.turlir.abakgists.api.data.GistJson;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class ApiClient {

    private static final String URL = "https://api.github.com";
    private final GitHubService mApi;

    public ApiClient(OkHttpClient okhttp) {
        GsonConverterFactory factory = GsonConverterFactory.create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(okhttp)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mApi = retrofit.create(GitHubService.class);
    }

    public Observable<List<GistJson>> publicGist(int page) {
        return mApi.publicGist(page);
    }

}
