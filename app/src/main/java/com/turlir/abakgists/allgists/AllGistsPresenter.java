package com.turlir.abakgists.allgists;


import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.turlir.abakgists.allgists.view.AllGistsFragment;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSituation;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

import rx.Subscription;
import timber.log.Timber;

public class AllGistsPresenter extends BasePresenter<AllGistsFragment> {

    private final ModelRequester mReq;
    private Subscription mCacheSubs;

    public AllGistsPresenter(ModelRequester repo) {
        mReq = repo;
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
                        loadPublicGists(-1);
                    }
                });
        addSubscription(subs);
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

    private static final class RepeatingError implements ErrorSituation {
        @Override
        public boolean should(Exception ex, boolean dataAvailable, boolean isErrorNow) {
            return isErrorNow;
        }
        @Override
        public void perform(@NonNull ErrorInterpreter v, Exception e) {
            v.blockingError("Увы, попытайтесь снова через некоторое время");
        }
    }

}
