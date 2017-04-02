package com.turlir.abakgists.base;


import android.content.Context;

import com.turlir.abakgists.R;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BasePresenter<T extends BaseView> {

    private T view;
    private final CompositeDisposable subs = new CompositeDisposable();

    public void attach(T view) {
        this.view = view;
    }

    public void detach() {
        view = null;
        subs.clear();
    }

    public final T getView() {
        return view;
    }

    public void add(Disposable s) {
        subs.add(s);
    }

    protected <E> ObservableTransformer<E, E> onIo() {
        return new SubscribeOnIoTransformer<>();
    }

    protected <E> ObservableTransformer<E, E> toMain() {
        return new ObserveOnMainTransformer<>();
    }

    protected CharSequence getErrorMessage(Context context, Throwable e) {
        return context.getText(R.string.error_general);
    }

    protected abstract class Handler<E> implements Observer<E> {

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(E value) {

        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Throwable e) {
            T view = getView();
            if (view != null) {
                CharSequence msg = getErrorMessage(view.getContext(), e);
                view.showError(msg.toString());
            }
        }
    }

    private static final class SubscribeOnIoTransformer<T> implements ObservableTransformer<T, T> {

        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            return upstream.subscribeOn(Schedulers.io());
        }
    }

    private static final class ObserveOnMainTransformer<T> implements ObservableTransformer<T, T> {

        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            return upstream.observeOn(AndroidSchedulers.mainThread());
        }
    }


}
