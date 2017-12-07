package com.turlir.abakgists.allgists;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.model.GistModel;

import java.util.Collections;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class GistLoader {

    private final GistListInteractor mInteractor;
    private final ListCombination.Callback<GistModel> mCallback;
    private final ErrorSelector mSelector;

    @Nullable
    private Subscription mCacheSubs;

    @NonNull
    private ListCombination<GistModel> mState;

    GistLoader(GistListInteractor interactor, ListCombination.Callback<GistModel> callback, ErrorSelector selector) {
        mInteractor = interactor;
        mCallback = callback;
        mSelector = selector;

        mState = new Start();
    }

    void resetState() {
        mInteractor.resetAccumulator();
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
                        mState = mState.error(e, mSelector);
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

        clearSubs();
        Subscription subs = mInteractor.update()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PutResults<GistLocal>>() {
                    @Override
                    public void onCompleted() {
                        // ignore
                    }
                    @Override
                    public void onError(Throwable e) {
                        mState = mState.error(e, mSelector);
                        mState.perform(mCallback);
                    }
                    @Override
                    public void onNext(PutResults<GistLocal> gistLocalPutResults) {
                        mState = mState.content(Collections.emptyList()); // hack!
                        mState.perform(mCallback);

                        resetState();
                        loadNewPage(GistListInteractor.IGNORE_SIZE);
                    }
                });
        memberSubs(subs);
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
