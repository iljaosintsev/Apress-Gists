package com.turlir.abakgists.api;

import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.api.data.GistMapper;
import com.turlir.abakgists.api.data.ListGistMapper;

import java.util.List;

import rx.Observable;

public class Repository {

    private final ApiClient mClient;
    private final ListGistMapper.Json mTransformer;

    public Repository(ApiClient client) {
        mClient = client;
        mTransformer = new ListGistMapper.Json(new GistMapper.Json());
    }

    /**
     * Скачивает очередную страницу с сервера и сохраняет ее в БД
     *
     * @param page номер загружаемой страницы, больше 1
     * @return сохраненные в БД элементы
     */
    public Observable<List<GistLocal>> server(int page) {
        return loadFromServer(page);
    }

    ///
    /// private
    ///

    private Observable<List<GistLocal>> loadFromServer(int page) {
        if (page < 1) throw new IllegalArgumentException();
        return mClient.publicGist(page)
                .doOnNext(new LagSideEffect(2500))
                .map(mTransformer);
    }

}
