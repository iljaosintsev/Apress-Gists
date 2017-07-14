package com.turlir.abakgists.allgists;


import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.base.ErrorInterpreter;
import com.turlir.abakgists.base.ErrorSituation;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.network.Repository;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
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
     */
    void loadPublicGists(final int currentSize) {
        removeCacheSubs();
        Subscription subs = mRepo
                .loadGists(currentSize)
                .distinctUntilChanged(new CycleRepeatingBreaker())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<GistModel>safeListFiltering())
                .subscribe(new GistDownloadHandler<List<GistModel>>() {
                    @Override
                    public void onNext(List<GistModel> value) {
                        Timber.d("onNext %d", value.size());
                        //noinspection ConstantConditions
                        getView().onGistLoaded(value);
                    }
                });
        addCacheSubs(subs);
    }

    void updateGist() {
        removeCacheSubs();
        Subscription subs = mRepo.reloadGists()
                .compose(this.<PutResults<GistModel>>defaultScheduler())
                .subscribe(new GistDownloadHandler<PutResults<GistModel>>() {
                    @Override
                    public void onNext(PutResults<GistModel> gistModelPutResults) {
                        if (getView() != null) {
                            getView().onUpdateSuccessful();
                            loadPublicGists(0);
                        }
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

    private void addCacheSubs(Subscription subs) {
        mCacheSubs = subs;
        addSubscription(mCacheSubs);
    }

    /**
     * Anti cycle-repeating request
     */
    private static class CycleRepeatingBreaker
            implements Func2<List<GistModel>, List<GistModel>, Boolean> {

        @Override
        public Boolean call(List<GistModel> prev, List<GistModel> now) {
            return prev.size() == now.size() && prev.size() == 0;
        }

    }

    private abstract class GistDownloadHandler<E> extends ErrorHandler<E> {

        @NonNull
        @Override
        protected ErrorSituation[] additionalSituation() {
            return new ErrorSituation[] { new RepeatingError() };
        }

        @Override
        protected boolean isError() {
            return getView() != null && getView().isError();
        }

        @Override
        protected boolean isDataAvailable() {
            return getView() != null && !getView().isEmpty();
        }

        @Override
        protected ErrorInterpreter interpreter() {
            return getView();
        }

    }

    private static final class RepeatingError implements ErrorSituation {
        @Override
        public boolean should(Exception ex, boolean dataAvailable, boolean isErrorNow) {
            return isErrorNow;
        }
        @Override
        public void perform(ErrorInterpreter v, Exception e) {
            v.blockingError("Увы, попытайтесь снова через некоторое время");
        }
    }

}
