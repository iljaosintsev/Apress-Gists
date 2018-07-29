package com.turlir.abakgists.allgists;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

class GistLoader {

    private final GistListInteractor mInteractor;
    private final ListCombination.Callback<GistModel> mCallback;
    private final ErrorSelector mSelector;
    private final ListCombination.ErrorProcessing mErrorProcessor;

    @Nullable
    private Disposable mCacheSubs;

    @NonNull
    private ListCombination<GistModel> mState;

    GistLoader(GistListInteractor interactor, ListCombination.Callback<GistModel> callback,
               ErrorSelector selector, ListCombination.ErrorProcessing errorProcessor) {
        mInteractor = interactor;
        mCallback = callback;
        mSelector = selector;
        mErrorProcessor = errorProcessor;

        mState = new Start();
    }

    void loadNewPage(int currentSize) {
        mState = mState.doLoad(currentSize);
        mState.perform(mCallback);

        clearSubs();
        Disposable subs = mInteractor.request(currentSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<GistModel>>() {
                    @Override
                    public void onComplete() {
                        // ignore
                    }
                    @Override
                    public void onError(Throwable e) {
                        mState = mState.error(e, mSelector, mErrorProcessor);
                        mState.perform(mCallback);
                    }
                    @Override
                    public void onNext(List<GistModel> gistModels) {
                        mState = mState.content(gistModels);
                        mState.perform(mCallback);
                    }
                });
        memberSubs(subs);
    }

    void refresh() {
        mState = mState.refresh();
        mState.perform(mCallback);

    }

    //

    private void clearSubs() {
        if (mCacheSubs != null && !mCacheSubs.isDisposed()) {
            mCacheSubs.dispose();
        }
    }

    private void memberSubs(Disposable subs) {
        mCacheSubs = subs;
    }
}
