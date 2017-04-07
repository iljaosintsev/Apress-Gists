package com.turlir.abakgists.allgists;


import android.util.Log;

import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.network.Repository;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AllGistsPresenter extends BasePresenter<AllGistsFragment> {

    private final Repository mRepo;
    private Subscription mCacheSubs;

    public AllGistsPresenter(Repository repo) {
        mRepo = repo;
    }

    void loadPublicGists(final int currentSize) {
        if (mCacheSubs != null) {
            if (!mCacheSubs.isUnsubscribed()) {
                removeSubscription(mCacheSubs);
            }
        }
        mCacheSubs = mRepo.loadGistsFromCache(currentSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Handler<List<Gist>>() {
                    @Override
                    public void onNext(List<Gist> value) {
                        Log.d("AllGistPresenter", "onNext " + value.size());
                        if (getView() != null) {
                            if (value.size() > 0) {
                                getView().onGistLoaded(value, currentSize, value.size());
                            } else {
                                loadFromServer(currentSize);
                            }
                        }
                    }
                });
        addSubscription(mCacheSubs);
    }

    private void loadFromServer(int currentSize) {
        Subscription subsToServer = mRepo.loadGistsFromServerAndPutCache(currentSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PutResults<Gist>>() {
                    @Override
                    public void call(PutResults<Gist> gistPutResults) {
                        Log.d("AllGistsPresenter", "fromServer obs " + gistPutResults.results().size());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        processError(throwable);
                    }
                });
        addSubscription(subsToServer);
    }

    void resetGist() {
        if (mCacheSubs != null) {
            if (!mCacheSubs.isUnsubscribed()) {
                removeSubscription(mCacheSubs);
            }
        }
        Subscription subs = mRepo.clearCache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action0() {
                    @Override
                    public void call() {
                        loadPublicGists(0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable e) {
                        processError(e);
                    }
                });
        addSubscription(subs);
    }
}
