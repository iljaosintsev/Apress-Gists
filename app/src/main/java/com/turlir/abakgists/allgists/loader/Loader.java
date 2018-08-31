package com.turlir.abakgists.allgists.loader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.turlir.abakgists.allgists.combination.InlineLoading;
import com.turlir.abakgists.allgists.combination.ListCombination;
import com.turlir.abakgists.allgists.combination.ListManipulator;
import com.turlir.abakgists.allgists.combination.Refresh;
import com.turlir.abakgists.allgists.combination.Start;
import com.turlir.abakgists.model.Identifiable;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.ResourceSingleObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import timber.log.Timber;

public abstract class Loader<T extends Identifiable<T>> {

    private final WindowedRepository<T> mInteractor;
    private final ListManipulator<T> mCallback;

    @NonNull
    private ListCombination<T> mState;
    private Disposable mDatabaseConnection = new CompositeDisposable();
    @Nullable
    private T mLast = null;
    private boolean isEnded;

    public Loader(WindowedRepository<T> interactor, ListManipulator<T> callback) {
        mInteractor = interactor;
        mCallback = callback;
        mState = new Start<>(mCallback);
    }

    public abstract boolean shouldRequest(boolean lessThan, boolean eqLast);

    public abstract boolean shouldRender(int size);

    public void firstPage() {
        if (!canLoad()) return;
        mState = new Start<>(mCallback);
        mState.perform();

        mDatabaseConnection = mInteractor.firstPage()
                .subscribeWith(new DatabaseSubscriber(true));
    }

    public void nextPage() {
        if (!canNext()) {
            Timber.d("loading next page is not allowed (is loading or last page reached)");
            return;
        }
        mDatabaseConnection.dispose();
        mDatabaseConnection = mInteractor.nextPage()
                .subscribeWith(new DatabaseSubscriber(false));
    }

    public void prevPage() {
        if (!canPrevious()) {
            Timber.d("loading previous page is not allowed (is loading or first page reached)");
            return;
        }
        mDatabaseConnection.dispose();
        mDatabaseConnection = mInteractor.prevPage()
                .doOnNext(nextItems -> isEnded = false)
                .subscribeWith(new DatabaseSubscriber(false));
    }

    public void updateGist(Window start) {
        if (!canLoad()) return;
        changeState(mState.refresh());
        mDatabaseConnection.dispose();
        mInteractor.loadAndReplace(start)
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

    public int size() {
        return mInteractor.range.prev().stop();
    }

    public boolean isDifferent(T now) {
        return mLast != null && now.isDifferent(mLast);
    }

    public void stop() {
        if (mDatabaseConnection != null) {
            mDatabaseConnection.dispose();
        }
    }

    public boolean canNext() {
        return canLoad() && mInteractor.range.hasNext() && !isEnded;
    }

    public boolean canPrevious() {
        return canLoad() && mInteractor.range.hasPrevious();
    }

    private void server(LoadablePage page) {
        mInteractor.loadAnPut(page)
                .doOnSuccess(i -> changeState(mState.doIntermediate()))
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

    private void changeState(ListCombination<T> now) {
        Timber.d("state change: leave %s, enter %s", mState.getClass().getSimpleName(), now.getClass().getSimpleName());
        mState = now;
        mState.perform();
    }

    private boolean canLoad() {
        return !(mState instanceof InlineLoading) && !(mState instanceof Refresh);
    }

    private class DatabaseSubscriber extends DisposableSubscriber<List<T>> {

        private boolean isFirstPage;

        DatabaseSubscriber(boolean isFirstPage) {
            this.isFirstPage = isFirstPage;
        }

        @Override
        public void onNext(List<T> nextItems) {
            if (isFirstPage) {
                itemsInFirstPage(nextItems);
            } else {
                suchNextItems(nextItems);
            }
        }

        @Override
        public void onError(Throwable t) {
            changeState(mState.error(t));
        }

        @Override
        public void onComplete() {
            //
        }

        private void itemsInFirstPage(List<T> nextItems) {
            int nowSize = nextItems.size();
            if (nowSize == 0) {
                LoadablePage page = mInteractor.page();
                server(page);
            } else {
                if (shouldRender(nowSize)) {
                    changeState(mState.content(nextItems));
                    mLast = nextItems.get(nextItems.size() - 1);
                }
            }
        }

        private void suchNextItems(List<T> nextItems) {
            int nowSize = nextItems.size();
            if (shouldRender(nowSize)) {
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
            }

            boolean lessThan = nowSize < mInteractor.range.count();
            T lastItem = nextItems.get(nowSize - 1);
            boolean eqLast = mLast == null || !lastItem.isDifferent(mLast);
            mLast = lastItem;

            if (shouldRequest(lessThan, eqLast)) {
                LoadablePage page = mInteractor.requiredPage();
                Timber.d("download required %d th page in %d items", page.number, page.size);
                server(page);
                changeState(mState.doLoad());
            }
        }
    }

}
