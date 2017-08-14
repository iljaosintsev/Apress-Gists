package com.turlir.abakgists.allgists;


import android.os.Bundle;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.turlir.abakgists.allgists.view.AllGistsFragment;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSituation;
import com.turlir.abakgists.base.erroring.RepeatingError;
import com.turlir.abakgists.model.GistModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import timber.log.Timber;

public class AllGistsPresenter extends BasePresenter<AllGistsFragment> {

    private static final String DATA_STATE = "DATA_STATE";

    private final ModelRequester mReq;
    private Subscription mCacheSubs;

    public AllGistsPresenter(ModelRequester repo) {
        mReq = repo;
    }

    @Override
    public void detach() {
        super.detach();
        mReq.state().clear();
    }

    /**
     * из локального кеша или сетевого запроса
     *
     */
    public void loadPublicGists(final int currentSize) {
        removeCacheSubs();
        Subscription subs = mReq.request(currentSize)
                .compose(this.<List<GistModel>>defaultScheduler())
                .compose(this.<GistModel>safeSubscribingWithList())
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

    public void updateGist() {
        removeCacheSubs();
        Subscription subs = mReq.update()
                .compose(this.<PutResults<GistLocal>>defaultScheduler())
                .compose(this.<PutResults<GistLocal>>safeSubscribing())
                .subscribe(new GistDownloadHandler<PutResults<GistLocal>>() {
                    @Override
                    public void onNext(PutResults<GistLocal> gistModelPutResults) {
                        //noinspection ConstantConditions
                        getView().onUpdateSuccessful();
                        loadPublicGists(ModelRequester.IGNORE_SIZE);
                    }
                });
        addSubscription(subs);
    }

    public void saveState(Bundle state) {
        if (state != null) {
            state.putParcelableArrayList(DATA_STATE, (ArrayList<GistModel>) mReq.state());
        }
    }

    public void restoreState(Bundle state) {
        if (state != null) {
            ArrayList<GistModel> models = state.getParcelableArrayList(DATA_STATE);
            mReq.state(models);
        }
    }

    public void first() {
        if (mReq.state().size() < 1) {
            loadPublicGists(0);
        } else {
            if (getView() != null) {
                getView().onGistLoaded(mReq.state());
            }
            loadPublicGists(ModelRequester.IGNORE_SIZE);
        }
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
