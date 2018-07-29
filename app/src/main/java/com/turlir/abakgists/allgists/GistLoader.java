package com.turlir.abakgists.allgists;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class GistLoader {

    private final GistListInteractor mInteractor;
    private final ListCombination.Callback<GistModel> mCallback;
    private final ErrorSelector mSelector;
    private final ListCombination.ErrorProcessing mErrorProcessor;

    @Nullable
    private Subscription mCacheSubs;

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
        Subscription subs = mInteractor.request(currentSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<GistModel>>() {
                    @Override
                    public void onCompleted() {
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
        if (mCacheSubs != null && !mCacheSubs.isUnsubscribed()) {
            mCacheSubs.unsubscribe();
        }
    }

    private void memberSubs(Subscription subs) {
        mCacheSubs = subs;
    }
}
