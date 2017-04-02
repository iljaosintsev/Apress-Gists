package com.turlir.abakgists.base;


import android.support.annotation.Nullable;

import com.turlir.abakgists.R;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BasePresenter<T extends BaseView> {

    private final CompositeDisposable subs = new CompositeDisposable();
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

    public void add(Disposable s) {
        subs.add(s);
    }

    protected <E> ObservableTransformer<E, E> subscribeIo() {
        return new SubscribeIo<>();
    }

    protected <E> ObservableTransformer<E, E> observeMain() {
        return new ObserveMain<>();
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

    protected abstract class Handler<E> implements Observer<E> {

        @Override
        public void onComplete() {

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
