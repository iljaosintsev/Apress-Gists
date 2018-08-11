package com.turlir.abakgists.allgists;

import android.support.annotation.NonNull;

import com.turlir.abakgists.allgists.combination.InlineLoading;
import com.turlir.abakgists.allgists.combination.ListCombination;
import com.turlir.abakgists.allgists.combination.ListManipulator;
import com.turlir.abakgists.allgists.combination.Refresh;
import com.turlir.abakgists.allgists.combination.Start;
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
        if (!canLoad()) return;
        mState = new Start(mCallback);
        mState.perform();

        mDatabaseConnection = mInteractor.subscribe()
                .subscribe(gistModels -> {
                    final Range range = mInteractor.range;
                    if (!range.isFull(gistModels.size())) {
                        LoadablePage page = range.page();
                        server(page.number, page.size);
                    }
                    if (gistModels.size() > 0) {
                        changeState(mState.content(gistModels));
                    }

                }, t -> {
                    changeState(mState.error(t));
                });
    }

    void nextPage() {
        if (!canNext()) return;
        mDatabaseConnection.dispose();
        mDatabaseConnection = mInteractor.nextPage()
                .subscribe(nextItems -> {
                   changeState(mState.content(nextItems));
                    if (!mInteractor.range.isFull(nextItems.size())) {
                        Range already = mInteractor.range.cut(nextItems.size());
                        Range required = mInteractor.range.diff(already);
                        LoadablePage page = required.page();
                        int number = page.number;
                        int size = page.size;
                        Timber.d("download required %d th page in %d items", number, size);
                        server(number, size);
                        changeState(mState.doLoad());
                    }
                }, t -> {
                    changeState(mState.error(t));
                });
    }

    int size() {
        return mInteractor.range.prev().absStop;
    }

    void stop() {
        mDatabaseConnection.dispose();
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
        Timber.v("StateChange leave %s, enter %s", mState.getClass().getSimpleName(), now.getClass().getSimpleName());
        mState = now;
        mState.perform();
    }

    private boolean canLoad() {
        return mState instanceof InlineLoading || !(mState instanceof Refresh);
    }

    private boolean canNext() {
        return canLoad() && mInteractor.range.hasNext();
    }

    private boolean canPrevious() {
        return canLoad() && mInteractor.range.hasPrevious();
    }
}
