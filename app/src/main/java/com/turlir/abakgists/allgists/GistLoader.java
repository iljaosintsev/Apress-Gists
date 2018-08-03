package com.turlir.abakgists.allgists;

import android.support.annotation.NonNull;

import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.model.GistModel;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.ResourceSingleObserver;
import timber.log.Timber;

class GistLoader {

    private final GistListInteractor mInteractor;
    private final ListCombination.Callback<GistModel> mCallback;
    private final ErrorSelector mSelector;
    private final ListCombination.ErrorProcessing mErrorProcessor;

    @NonNull
    private ListCombination<GistModel> mState;
    private Disposable mDatabaseConnection;

    GistLoader(GistListInteractor interactor, ListCombination.Callback<GistModel> callback,
               ErrorSelector selector, ListCombination.ErrorProcessing errorProcessor) {
        mInteractor = interactor;
        mCallback = callback;
        mSelector = selector;
        mErrorProcessor = errorProcessor;

        mState = new Start();
    }

    void firstPage() {
        mState = new Start();
        mState = mState.doLoad();
        mState.perform(mCallback);

        mDatabaseConnection = mInteractor.subscribe()
                .subscribe(gistModels -> {
                    if (!mInteractor.range.isFull(gistModels.size())) {
                        server(1, mInteractor.range.count());
                    }
                    if (gistModels.size() > 0) {
                        mState = mState.content(gistModels);
                        mState.perform(mCallback);
                    }

                }, t -> {
                    mState = mState.error(t, mSelector, mErrorProcessor);
                    mState.perform(mCallback);
                });
    }

    void nextPage() {
        if (mState instanceof InlineLoading || mState instanceof Refresh) {
            return;
        }
        mDatabaseConnection.dispose();
        mDatabaseConnection = mInteractor.nextPage()
                .subscribe(nextItems -> {
                    mState = mState.content(nextItems);
                    mState.perform(mCallback);
                    if (!mInteractor.range.isFull(nextItems.size())) {
                        int[] spec = mInteractor.range.specRequiredItems(nextItems.size());
                        int page = spec[0];
                        int one = spec[1];
                        Timber.d("needs load %d th page in %d items", page, one);

                        server(page, one);
                        mState = mState.doLoad();
                        mState.perform(mCallback);
                    }
                });
    }

    private void server(int page, int perPage) {
        mInteractor.server(page, perPage)
                .subscribe(new ResourceSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer count) {
                        dispose();
                    }
                    @Override
                    public void onError(Throwable e) {
                        mState = mState.error(e, mSelector, mErrorProcessor);
                        mState.perform(mCallback);
                        dispose();
                    }
                });
    }

    public int size() {
        return mInteractor.range.prev().absStop;
    }
}
