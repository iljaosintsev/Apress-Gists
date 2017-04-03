package com.turlir.abakgists.base;


import android.support.annotation.Nullable;

import com.turlir.abakgists.R;

import java.lang.ref.WeakReference;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter<T extends BaseView> {

    private static final ObservableSchedulersTransformer STANDARD_SCHEDULER
            = new ObservableSchedulersTransformer();

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

    public void addSubscription(Subscription s) {
        subs.add(s);
    }

    protected <E> Observable.Transformer<E, E> defaultSchedule() {
        return (Observable.Transformer<E, E>) STANDARD_SCHEDULER;
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

    protected abstract class Handler<E> extends Subscriber<E> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            T view = getView();
            if (view != null) {
                CharSequence msg = view.getContext().getText(R.string.error_general);
                view.showError(msg.toString());
            }
        }
    }


}
