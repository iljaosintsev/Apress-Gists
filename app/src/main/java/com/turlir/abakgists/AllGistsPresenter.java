package com.turlir.abakgists;


import android.renderscript.RSInvalidStateException;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.network.ApiClient;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AllGistsPresenter extends BasePresenter<AllGistsFragment> {

    private static final int PAGE_SIZE = 30;

    private final ApiClient mClient;
    private final StorIOSQLite mDatabase;

    public AllGistsPresenter(ApiClient client, StorIOSQLite database) {
        mClient = client;
        mDatabase = database;
    }

    void loadPublicGists(int currentSize) {
        int tmp = Math.max(currentSize, PAGE_SIZE);
        int page = tmp  / PAGE_SIZE;

        Observable<List<Gist>> dataCache = mDatabase.get()
                .listOfObjects(Gist.class)
                .withQuery(
                        Query.builder()
                                .table("gists")
                                .limit(currentSize, PAGE_SIZE)
                                .orderBy("_id ASC")
                                .build()
                )
                .prepare()
                .asRxObservable()
                .map(new Func1<List<Gist>, List<Gist>>() {
                    @Override
                    public List<Gist> call(List<Gist> gists) {
                        if (gists.size() < 1) {
                            throw new IllegalStateException("База данных пуста");
                        }
                        return gists;
                    }
                });

        addSubscription(mClient.publicGist(page)
                .doOnNext(new Action1<List<Gist>>() {
                    @Override
                    public void call(List<Gist> gists) {
                        // не исполняется в случае использования кеша
                        if (gists != null && !gists.isEmpty()) {
                            mDatabase.put()
                                    .objects(gists)
                                    .prepare()
                                    .executeAsBlocking();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(dataCache)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Handler<List<Gist>>() {
                    @Override
                    public void onNext(List<Gist> value) {
                        if (getView() != null) {
                            if (value.size() < PAGE_SIZE) {
                                // достигнут конец списка
                                getView().stopGistLoad();
                            } else {
                                getView().onGistLoaded(value);
                            }
                        }
                    }
                }));
    }

}
