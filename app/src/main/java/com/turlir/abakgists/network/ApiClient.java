package com.turlir.abakgists.network;


import com.turlir.abakgists.model.Gist;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String URL = "https://api.github.com";
    private final GitHubService mApi;

    public ApiClient(OkHttpClient okhttp) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(okhttp)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mApi = retrofit.create(GitHubService.class);
    }

    public Observable<List<Gist>> publicGist(int page) {
        return mApi.publicGist(page);
    }

}
