package com.turlir.abakgists;


import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.network.ApiClient;

import java.util.List;

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
        currentSize = Math.max(currentSize, PAGE_SIZE);
        int page = currentSize / PAGE_SIZE;
        addSubscription(mClient.publicGist(page)
                .doOnNext(new Action1<List<Gist>>() {
                    @Override
                    public void call(List<Gist> gists) {
                        if (gists != null && !gists.isEmpty()) {
                            mDatabase.put()
                                    .objects(gists)
                                    .prepare()
                                    .executeAsBlocking();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
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
