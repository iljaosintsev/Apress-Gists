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
import io.reactivex.subscribers.DisposableSubscriber;
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
                .subscribeWith(new DatabaseSubscriber(true));
    }

    void nextPage() {
        if (!canNext()) {
            Timber.d("loading next page is not allowed (is loading or last page reached)");
            return;
        }
        mDatabaseConnection.dispose();
        mDatabaseConnection = mInteractor.nextPage()
                .subscribeWith(new DatabaseSubscriber(false));
    }

    void prevPage() {
        if (!canPrevious()) {
            Timber.d("loading previous page is not allowed (is loading or first page reached)");
            return;
        }
        mDatabaseConnection.dispose();
        mDatabaseConnection = mInteractor.prevPage()
                .doOnNext(nextItems -> isEnded = false)
                .subscribeWith(new DatabaseSubscriber(false));
    }

    void updateGist() {
        if (!canLoad()) return;
        changeState(mState.refresh());
        mDatabaseConnection.dispose();
        mInteractor.loadAndReplace()
                .subscribe(new ResourceSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer deleted) {
                        dispose();
                        Timber.d("successful deleted %d items", deleted);
                        changeState(mState.doIntermediate());
                        firstPage();
                    }
                    @Override
                    public void onError(Throwable e) {
                        dispose();
                        changeState(mState.error(e));
                    }
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

    boolean canNext() {
        return canLoad() && mInteractor.range.hasNext() && !isEnded;
    }

    boolean canPrevious() {
        return canLoad() && mInteractor.range.hasPrevious();
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

    private void onNextItems(List<GistModel> nextItems, boolean isFirstPage) {
        if (isFirstPage) {
            itemsInFirstPage(nextItems);
        } else {
            suchNextItems(nextItems);
        }
    }

    private void itemsInFirstPage(List<GistModel> nextItems) {
        int nowSize = nextItems.size();
        if (nowSize == 0) {
            LoadablePage page = mInteractor.range.page();
            server(page);
        } else {
            changeState(mState.content(nextItems));
            mLast = nextItems.get(nextItems.size() - 1);
        }
    }

    private void suchNextItems(List<GistModel> nextItems) {
        int nowSize = nextItems.size();
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

    private void changeState(ListCombination<GistModel> now) {
        Timber.d("state change: leave %s, enter %s", mState.getClass().getSimpleName(), now.getClass().getSimpleName());
        mState = now;
        mState.perform();
    }

    private boolean canLoad() {
        return !(mState instanceof InlineLoading) && !(mState instanceof Refresh);
    }

    private class DatabaseSubscriber extends DisposableSubscriber<List<GistModel>> {

        private boolean isFirstPage;

        DatabaseSubscriber(boolean isFirstPage) {
            this.isFirstPage = isFirstPage;
        }

        @Override
        public void onNext(List<GistModel> gistModels) {
            onNextItems(gistModels, isFirstPage);
        }

        @Override
        public void onError(Throwable t) {
            changeState(mState.error(t));
        }

        @Override
        public void onComplete() {
            //
        }
    }
}
