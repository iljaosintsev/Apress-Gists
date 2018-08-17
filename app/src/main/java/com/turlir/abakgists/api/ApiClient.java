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

    private final GitHubService mApi;
    private final Shift mShift;

    public ApiClient(OkHttpClient okhttp) {
        GsonConverterFactory factory = GsonConverterFactory.create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(okhttp)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mApi = retrofit.create(GitHubService.class);
        mShift = new Shift();
    }

    public Single<List<GistJson>> publicGist(final int page, final int perPage) {
        int nowPage = mShift.shift(page, perPage);
        return mApi.publicGist(nowPage, perPage)
                .doOnSuccess(new LagSideEffect())
                .doOnError(new LagSideEffect());
    }

    static class Shift {
        private static final int MAX_PAGE = 20;
        private static final int PER_PAGE = 15;

        public int shift(int page, int perPage) {
            if (perPage == PER_PAGE) {
                page = MAX_PAGE - page + 1;
                return page;
            } else {
                int scaled = perPage / 15;
                int demo = MAX_PAGE / scaled;
                return demo - page + 1;
            }
        }
    }
}
