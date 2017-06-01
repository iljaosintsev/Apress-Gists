package com.turlir.abakgists.allgists;


import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.network.Repository;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class AllGistsPresenter extends BasePresenter<AllGistsFragment> {

    private final Repository mRepo;
    private Subscription mCacheSubs;

    public AllGistsPresenter(Repository repo) {
        mRepo = repo;
    }

    /**
     * из локального кеша или сетевого запроса
     *
     * @param currentSize текущий размер списка (для пагинации)
     */
    void loadPublicGists(final int currentSize) {
        removeCacheSubs();
        mCacheSubs = mRepo.loadGistsFromCache(currentSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Handler<List<GistModel>>() {
                    @Override
                    public void onNext(List<GistModel> value) {
                        Timber.d("onNext %d", value.size());
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

    /**
     * сбросить кеш, загрузить и сохранить свежие результаты
     */
    void resetGist() {
        removeCacheSubs();
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

    private void removeCacheSubs() {
        if (mCacheSubs != null) {
            if (!mCacheSubs.isUnsubscribed()) {
                removeSubscription(mCacheSubs);
            }
        }
    }

    private void loadFromServer(int currentSize) {
        Subscription subsToServer = mRepo.loadGistsFromServerAndPutCache(currentSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PutResults<GistModel>>() {
                    @Override
                    public void call(PutResults<GistModel> gistPutResults) {
                        Timber.d("fromServer obs %d", gistPutResults.results().size());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        processError(throwable);
                    }
                });
        addSubscription(subsToServer);
    }
}
