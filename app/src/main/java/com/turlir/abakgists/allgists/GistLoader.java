package com.turlir.abakgists.allgists;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.turlir.abakgists.allgists.combination.InlineLoading;
import com.turlir.abakgists.allgists.combination.ListCombination;
import com.turlir.abakgists.allgists.combination.ListManipulator;
import com.turlir.abakgists.allgists.combination.Refresh;
import com.turlir.abakgists.allgists.combination.Start;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.ResourceSingleObserver;
import timber.log.Timber;

class GistLoader {

    private final GistListInteractor mInteractor;
    private final ListManipulator<GistModel> mCallback;

    @NonNull
    private ListCombination<GistModel> mState;
    private Disposable mDatabaseConnection;
    @Nullable
    private GistModel mLast = null;
    private boolean isEnded;

    GistLoader(GistListInteractor interactor, ListManipulator<GistModel> callback) {
        mInteractor = interactor;
        mCallback = callback;

        mState = new Start(mCallback);
    }

    void firstPage() {
        if (!canLoad()) return;
        mState = new Start(mCallback);
        mState.perform();

        mDatabaseConnection = mInteractor.firstPage()
                .subscribe(gistModels -> {
                    final Range range = mInteractor.range;
                    if (gistModels.size() == 0 && !isEnded) {
                        LoadablePage page = range.page();
                        server(page);
                    }
                    if (gistModels.size() > 0) {
                        changeState(mState.content(gistModels));
                        mLast = gistModels.get(gistModels.size() - 1);
                    }
                    isEnded = range.count() != gistModels.size();

                }, t -> {
                    changeState(mState.error(t));
                });
    }

    void nextPage() {
        if (!canNext()) {
            Timber.d("loading next page is not allowed (is loading or last page reached)");
            return;
        }
        mDatabaseConnection.dispose();
        mDatabaseConnection = mInteractor.nextPage()
                .subscribe(nextItems -> {
                    onNextItems(nextItems);
                }, t -> {
                    changeState(mState.error(t));
                });
    }

    private void onNextItems(List<GistModel> nextItems) {
        if (!canLoad()) { // loading in process
            Timber.d("updating list when loading the next page");
            mState.content(nextItems).perform(); // side effect without state change
            mState.perform(); // repeat loading
            mLast = nextItems.get(nextItems.size() - 1);
            return;
        } else {
            Timber.d("updating list direct");
            changeState(mState.content(nextItems)); // perform
        }

        int nowSize = nextItems.size();
        boolean lessThan = nowSize < mInteractor.range.count();
        GistModel lastItem = nextItems.get(nowSize - 1);
        boolean eqLast = mLast == null || !lastItem.isDifferent(mLast);
        mLast = lastItem;

        if (lessThan && canNext() && eqLast) {
            Range already = mInteractor.range.cut(mInteractor.range.addition);
            Range required = mInteractor.range.diff(already);
            LoadablePage page = required.page();
            Timber.d("download required %d th page in %d items", page.number, page.size);
            server(page);
            changeState(mState.doLoad());
        }
    }

    void prevPage() {
        if (!canPrevious()) {
            Timber.d("loading previous page is not allowed (is loading or first page reached)");
            return;
        }
        mDatabaseConnection.dispose();
        mDatabaseConnection = mInteractor.prevPage()
                .doOnNext(nextItems -> {
                    isEnded = false;
                })
                .subscribe(nextItems -> {
                    changeState(mState.content(nextItems));
                    mLast = nextItems.get(nextItems.size() - 1);
                }, t -> {
                    changeState(mState.error(t));
                });
    }

    int size() {
        return mInteractor.range.prev().absStop;
    }

    boolean isDifferent(GistModel now) {
        return mLast != null && now.isDifferent(mLast);
    }

    void stop() {
        if (mDatabaseConnection != null) {
            mDatabaseConnection.dispose();
        }
    }

    private void server(LoadablePage page) {
        mInteractor.server(page)
                .doOnSuccess(i -> {
                    changeState(mState.doIntermediate());
                })
                .subscribe(new ResourceSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer count) {
                        isEnded = count < page.size;
                        if (isEnded) {
                            Timber.d("gists list ended");
                        }
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
        Timber.d("state change: leave %s, enter %s", mState.getClass().getSimpleName(), now.getClass().getSimpleName());
        mState = now;
        mState.perform();
    }

    private boolean canLoad() {
        return !(mState instanceof InlineLoading) && !(mState instanceof Refresh);
    }

    public boolean canNext() {
        return canLoad() && mInteractor.range.hasNext() && !isEnded;
    }

    public boolean canPrevious() {
        return canLoad() && mInteractor.range.hasPrevious();
    }
}
