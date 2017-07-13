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
import rx.functions.Func2;
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
        Subscription subs = mRepo
                .loadGistsFromCache()
                .distinctUntilChanged(new CycleRepeatingBreaker())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Handler<List<GistModel>>() {
                    @Override
                    public void onNext(List<GistModel> value) {
                        Timber.d("onNext %d", value.size());
                        if (getView() != null) {
                            if (value.size() > currentSize) {
                                getView().onGistLoaded(value);
                            } else {
                                loadFromServer(currentSize);
                            }
                        }
                    }
                });
        addCacheSubs(subs);
    }

    /**
     * сбросить кеш, загрузить и сохранить свежие результаты
     */
    void resetGist() {
        removeCacheSubs();
        Subscription subs = mRepo
                .clearCache()
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
        addCacheSubs(subs);
    }

    private void loadFromServer(int currentSize) {
        Subscription subsToServer = mRepo
                .loadGistsFromServerAndPutCache(currentSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Handler<PutResults<GistModel>>() {
                    @Override
                    public void onNext(PutResults<GistModel> result) {
                        Timber.d("fromServer obs %d", result.results().size());
                    }
                });
        addSubscription(subsToServer);
    }

    private void removeCacheSubs() {
        if (mCacheSubs != null) {
            if (!mCacheSubs.isUnsubscribed()) {
                removeSubscription(mCacheSubs);
            }
        }
    }

    private void addCacheSubs(Subscription subs) {
        mCacheSubs = subs;
        addSubscription(mCacheSubs);
    }


    /**
     * Anti cycle-repeating request
     */
    private static class CycleRepeatingBreaker implements Func2<List<GistModel>, List<GistModel>, Boolean> {
        @Override
        public Boolean call(List<GistModel> prev, List<GistModel> now) {
            return prev.size() == now.size() && prev.size() == 0;
        }
    }
}
