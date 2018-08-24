package com.turlir.abakgists.base;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.turlir.abakgists.R;
import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.base.erroring.ErrorSituation;
import com.turlir.abakgists.base.erroring.TroubleSelector;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public abstract class BasePresenter<T extends BaseView> {

    private static final ObservableTransformer STANDARD_SCHEDULER
            = new ObservableSchedulersTransformer();

    private final ListCompositeDisposable subs = new ListCompositeDisposable();

    private WeakReference<T> view = new WeakReference<>(null);

    public void attach(T view) {
        this.view = new WeakReference<>(view);
    }

    public void detach() {
        subs.clear();
        view.clear();
    }

    ///
    /// internal usage only
    ///

    @Nullable
    protected final T getView() {
        return view.get();
    }

    protected void addSubscription(Disposable s) {
        subs.add(s);
    }

    protected void removeSubscription(Disposable s) {
        subs.remove(s);
    }

    protected <E> ObservableTransformer<E, E> defaultScheduler() {
        return STANDARD_SCHEDULER;
    }

    ///
    /// classes
    ///

    public abstract class ErrorHandler<E> extends Handler<E> {

        private final ErrorSelector mRobot;

        protected ErrorHandler() {
            mRobot = new TroubleSelector(additionalSituation());
        }

        @Override
        public void onError(Throwable throwable) {
            if (getView() == null) return;

            if (throwable instanceof Exception) {
                Exception exception = (Exception) throwable;
                Timber.e(exception);

                ErrorInterpreter interpreter = interpreter();
                if (interpreter != null) {
                    ErrorSituation callback =
                            mRobot.select(exception, isDataAvailable(), isError());

                    callback.perform(interpreter, exception, getView().getContext().getResources());
                }

            } else {
                throw new RuntimeException(throwable);
            }
        }

        @NonNull
        protected ErrorSituation[] additionalSituation() {
            return new ErrorSituation[0];
        }

        protected abstract boolean isError();

        protected abstract boolean isDataAvailable();

        @Nullable
        protected abstract ErrorInterpreter interpreter();
    }

    protected abstract class Handler<E> extends DisposableObserver<E> {

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e);
            T view = getView();
            if (view != null) {
                CharSequence msg = view.getContext().getString(R.string.error_general);
                view.showError(msg.toString());
            }
        }
    }

    ///
    /// private inner classes
    ///

    /**
     * Подписка на безопасных потоках
     *
     * @param <T> тип последовательности
     */
    private static final class ObservableSchedulersTransformer<T>
            implements ObservableTransformer<T, T> {

        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            return upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

}
