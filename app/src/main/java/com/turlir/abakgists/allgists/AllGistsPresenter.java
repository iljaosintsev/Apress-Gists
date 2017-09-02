package com.turlir.abakgists.allgists;


import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.turlir.abakgists.allgists.view.AllGistsFragment;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.base.App;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSituation;
import com.turlir.abakgists.base.erroring.RepeatingError;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import timber.log.Timber;

public class AllGistsPresenter extends BasePresenter<AllGistsFragment> {

    @Inject
    GistListInteractor _interactor;

    private Subscription mCacheSubs;

    public AllGistsPresenter() {
        App.getComponent().inject(this);
    }

    /**
     * из локального кеша или сетевого запроса
     *
     */
    public void loadPublicGists(final int currentSize) {
        removeCacheSubs();
        Subscription subs = _interactor.request(currentSize)
                .compose(this.<List<GistModel>>defaultScheduler())
                .subscribe(new GistDownloadHandler<List<GistModel>>() {
                    @Override
                    public void onNext(List<GistModel> value) {
                        if (getView() != null) {
                            Timber.d("onNext %d", value.size());
                            getView().onGistLoaded(value);
                        }
                    }
                });
        addCacheSubs(subs);
    }

    public void updateGist() {
        removeCacheSubs();
        Subscription subs = _interactor.update()
                .compose(this.<PutResults<GistLocal>>defaultScheduler())
                .subscribe(new GistDownloadHandler<PutResults<GistLocal>>() {
                    @Override
                    public void onNext(PutResults<GistLocal> gistModelPutResults) {
                        if (getView() != null) {
                            getView().onUpdateSuccessful();
                            loadPublicGists(GistListInteractor.IGNORE_SIZE);
                        }
                    }
                });
        addSubscription(subs);
    }

    public void first() {
        App.getComponent().inject(this);
        loadPublicGists(0);
    }

    public void again() {
        if (getView() != null) {
            getView().onGistLoaded(_interactor.accumulator());
        }
        loadPublicGists(GistListInteractor.IGNORE_SIZE);
    }

    private void removeCacheSubs() {
        if (mCacheSubs != null && !mCacheSubs.isUnsubscribed()) {
            removeSubscription(mCacheSubs);
        }
    }

    private void addCacheSubs(Subscription subs) {
        mCacheSubs = subs;
        addSubscription(mCacheSubs);
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

}
