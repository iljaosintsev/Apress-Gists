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

    protected <E> ObservableTransformer<E, E> subscribeIo() {
        return new SubscribeIo<>();
    }

    protected <E> ObservableTransformer<E, E> observeMain() {
        return new ObserveMain<>();
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
            e.printStackTrace();
            if (view != null) {
                CharSequence msg = view.getContext().getText(R.string.error_general);
                view.showError(msg.toString());
            }
        }
    }

    private static final class SubscribeIo<T> implements ObservableTransformer<T, T> {

        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            return upstream.subscribeOn(Schedulers.io());
        }
    }

    private static final class ObserveMain<T> implements ObservableTransformer<T, T> {

        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            return upstream.observeOn(AndroidSchedulers.mainThread());
        }
    }


}
