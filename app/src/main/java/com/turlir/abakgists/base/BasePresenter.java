package com.turlir.abakgists.base;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.turlir.abakgists.R;
import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.base.erroring.ErrorSituation;
import com.turlir.abakgists.base.erroring.TroubleSelector;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter<T extends BaseView> {

    private static final ObservableSchedulersTransformer STANDARD_SCHEDULER
            = new ObservableSchedulersTransformer();

    private final SafeListFiltering SAFE_LIST_FILTERING = new SafeListFiltering();
    private final SafeFiltering SAFE_FILTERING = new SafeFiltering();

    private final CompositeSubscription subs = new CompositeSubscription();

    private WeakReference<T> view = new WeakReference<>(null);

    public void attach(T view) {
        this.view = new WeakReference<>(view);
    }

    public void detach() {
        subs.clear();
    }

    @Nullable
    public final T getView() {
        return view.get();
    }

    protected void addSubscription(Subscription s) {
        subs.add(s);
    }

    protected void removeSubscription(Subscription s) {
        subs.remove(s);
    }

    protected <E> Observable.Transformer<E, E> defaultScheduler() {
        return STANDARD_SCHEDULER;
    }

    protected <E> Observable.Transformer<List<E>, List<E>> safeListFiltering() {
        return SAFE_LIST_FILTERING;
    }

    protected <B> Observable.Transformer<B, B> safeFiltering() {
        return SAFE_FILTERING;
    }

    private static final class ObservableSchedulersTransformer<T>
            implements Observable.Transformer<T, T> {
        @Override
        public Observable<T> call(Observable<T> upstream) {
            return upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    protected abstract class ErrorHandler<E> extends Handler<E> {

        private final ErrorSelector mRobot;

        protected ErrorHandler() {
            super();
            mRobot = new TroubleSelector(additionalSituation());
        }

        @Override
        public void onError(Throwable throwable) {
            ErrorInterpreter interpreter = interpreter();
            if (throwable instanceof Exception && interpreter != null) {
                ErrorSituation callback =
                        mRobot.select((Exception) throwable, isDataAvailable(), isError());

                callback.perform(interpreter, (Exception) throwable);
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

    protected abstract class Handler<E> extends Subscriber<E> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            processError(e);
        }
    }

    private void processError(Throwable e) {
        e.printStackTrace();
        T view = getView();
        if (view != null) {
            CharSequence msg = view.getContext().getString(R.string.error_general);
            view.showError(msg.toString());
        }
    }

    /**
     * Действительна ли сейчас ссылка на вью
     * @param <B> тип последовательности
     */
    private class SafeFiltering<B> implements Observable.Transformer<B, B> {

        @Override
        public Observable<B> call(Observable<B> obs) {
            return obs.filter(new Func1<B, Boolean>() {
                @Override
                public Boolean call(B v) {
                    return getView() != null;
                }
            });
        }
    }

    /**
     * Тоже что и {@code SafeFiltering} плюс проверка на то, что список не пустой и не null
     * @param <V> тип элементов списка последовательности
     */
    private class SafeListFiltering<V> implements Observable.Transformer<List<V>, List<V>> {
        @Override
        public Observable<List<V>> call(Observable<List<V>> obs) {
            return obs
                    .compose(new SafeFiltering<List<V>>())
                    .filter(new Func1<List<V>, Boolean>() {
                        @Override
                        public Boolean call(List<V> data) {
                            return data != null && data.size() > 0;
                        }
                    });
        }
    }
}
