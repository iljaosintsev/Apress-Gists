package com.turlir.abakgists.allgists;

import android.support.annotation.NonNull;

import com.turlir.abakgists.model.GistModel;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.ResourceSingleObserver;
import timber.log.Timber;

class GistLoader {

    private final GistListInteractor mInteractor;
    private final ListManipulator<GistModel> mCallback;

    @NonNull
    private ListCombination<GistModel> mState;
    private Disposable mDatabaseConnection;

    GistLoader(GistListInteractor interactor, ListManipulator<GistModel> callback) {
        mInteractor = interactor;
        mCallback = callback;

        mState = new Start(mCallback);
    }

    void firstPage() {
        mState = new Start(mCallback);
        changeState(mState.doLoad());

        mDatabaseConnection = mInteractor.subscribe()
                .subscribe(gistModels -> {
                    if (!mInteractor.range.isFull(gistModels.size())) {
                        server(1, mInteractor.range.count());
                    }
                    if (gistModels.size() > 0) {
                        changeState(mState.content(gistModels));
                    }

                }, t -> {
                    changeState(mState.error(t));
                });
    }

    void nextPage() {
        if (mState instanceof InlineLoading || mState instanceof Refresh) {
            return;
        }
        mDatabaseConnection.dispose();
        mDatabaseConnection = mInteractor.nextPage()
                .subscribe(nextItems -> {
                   changeState(mState.content(nextItems));
                    if (!mInteractor.range.isFull(nextItems.size())) {
                        int[] spec = mInteractor.range.specRequiredItems(nextItems.size());
                        int page = spec[0];
                        int one = spec[1];
                        Timber.d("needs load %d th page in %d items", page, one);

                        server(page, one);
                        changeState(mState.doLoad());
                    }
                }, t -> {
                    changeState(mState.error(t));
                });
    }

    public int size() {
        return mInteractor.range.prev().absStop;
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
                        changeState(mState.error(e));
                        dispose();
                    }
                });
    }

    private void changeState(ListCombination<GistModel> now) {
        Timber.v("StateChange", "onExit " + mState.getClass().getSimpleName());
        mState = now;
        Timber.v("StateChange", "onStart " + now.getClass().getSimpleName());
        mState.perform();
    }
}
