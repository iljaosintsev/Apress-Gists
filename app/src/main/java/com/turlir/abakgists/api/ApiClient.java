package com.turlir.abakgists.api;


import com.turlir.abakgists.api.data.GistJson;
import com.turlir.abakgists.api.data.GistOwnerJson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class ApiClient {

    private static final String URL = "https://api.github.com";
    private final GitHubService mApi;

    private static final SimpleDateFormat SERVER_FORMAT
            = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault());

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
        if (page < 2) {
            return mApi.publicGist(page);
        } else {
            List<GistJson> list = generateResponse();
            return Observable.just(list);
        }
    }

    private List<GistJson> generateResponse() {
        List<GistJson> list = new ArrayList<>(30);
        for (int i = 0; i < 30; i++) {
            GistJson json = new GistJson();
            json.id = UUID.randomUUID().toString();
            json.url = "https://status.github.com/messages";
            json.created = SERVER_FORMAT.format(new Date());
            json.description = "";
            json.owner = new GistOwnerJson(
                    "personal generator",
                    "https://avatars1.githubusercontent.com/u/3526847"
            );
            list.add(json);
        }
        return list;
    }

}
