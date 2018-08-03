package com.turlir.abakgists.api;


import com.turlir.abakgists.api.data.GistJson;

import java.util.List;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String URL = "https://api.github.com";
    private static final int MAX_PAGE = 20;

    private final GitHubService mApi;

    public ApiClient(OkHttpClient okhttp) {
        GsonConverterFactory factory = GsonConverterFactory.create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(okhttp)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mApi = retrofit.create(GitHubService.class);
    }

    public Single<List<GistJson>> publicGist(int page, int perPage) {
        page = MAX_PAGE - page + 1;
        return mApi.publicGist(page, perPage)
                .doOnSuccess(new LagSideEffect(5500))
                .doOnError(new LagSideEffect(5500));
    }



}
