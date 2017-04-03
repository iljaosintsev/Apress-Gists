package com.turlir.abakgists;


import android.renderscript.RSInvalidStateException;
import android.util.Log;

import com.pushtorefresh.storio.sqlite.Changes;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.model.GistSQLiteTypeMapping;
import com.turlir.abakgists.network.ApiClient;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class AllGistsPresenter extends BasePresenter<AllGistsFragment> {

    private static final int PAGE_SIZE = 30;

    private final ApiClient mClient;
    private final StorIOSQLite mDatabase;

    public AllGistsPresenter(ApiClient client, StorIOSQLite database) {
        mClient = client;
        mDatabase = database;
    }

    void loadPublicGists(final int currentSize) {

        final Observable<List<Gist>> serverData = mClient.publicGist(1)
                .map(new Func1<List<Gist>, List<Gist>>() {
                    @Override
                    public List<Gist> call(List<Gist> gists) {
                        for (Gist item : gists) {
                            if (item.owner != null) {
                                item.ownerLogin = item.owner.login;
                                item.ownerAvatarUrl = item.owner.avatarUrl;
                            }
                        }
                        return gists;
                    }
                });

        final Observable<List<Gist>> cacheData = mDatabase.get()
                .listOfObjects(Gist.class)
                .withQuery(
                        Query.builder()
                                .table("gists")
                                .build()
                )
                .prepare()
                .asRxObservable();


        Subscription subs = cacheData
                .map(new Func1<List<Gist>, List<Gist>>() {
                            @Override
                            public List<Gist> call(List<Gist> gists) {
                                if (gists.size() < 1) {
                                    gists = serverData.toBlocking().first();
                                    mDatabase.put().objects(gists).prepare().executeAsBlocking();
                                }
                                return gists;
                            }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Handler<List<Gist>>() {
                    @Override
                    public void onNext(List<Gist> value) {
                        if (getView() != null) {
                            getView().onGistLoaded(value, currentSize, PAGE_SIZE);
                        }
                    }
                });

        addSubscription(subs);
    }

}
