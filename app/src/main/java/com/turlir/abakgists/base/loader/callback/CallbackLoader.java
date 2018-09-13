package com.turlir.abakgists.base.loader.callback;

import android.support.annotation.NonNull;

import com.turlir.abakgists.base.loader.SimpleLoader;
import com.turlir.abakgists.base.loader.Window;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class CallbackLoader<T> {

    private final SimpleLoader<T> mLoader;
    private final CompositeDisposable mResource;

    @NonNull
    private DataCallback<T> mCallback;

    public CallbackLoader(SimpleLoader<T> loader) {
        this(loader, new EmptyCallback<>());
    }

    private CallbackLoader(SimpleLoader<T> loader, @NonNull DataCallback<T> callback) {
        mLoader = loader;
        mCallback = callback;
        mResource = new CompositeDisposable();
    }

    public void setCallback(@NonNull DataCallback<T> callback) {
        mCallback = callback;
    }

    @NonNull
    public DataCallback<T> getCallback() {
        return mCallback;
    }

    public void stop() {
        mResource.clear();
    }

    public void currentPage() {
        subscribe(mLoader.currentPage());
    }

    public void nextPage() {
        subscribe(mLoader.nextPage());
    }

    public void prevPage() {
        subscribe(mLoader.prevPage());
    }

    public Window getWindow() {
        return mLoader.getWindow();
    }

    public void setWindow(Window w) {
        mLoader.setWindow(w);
    }

    private void subscribe(Flowable<List<T>> source) {
        stop();
        mResource.add(source
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<T>>() {
                    @Override
                    public void onNext(List<T> ts) {
                        mCallback.content(ts, getWindow(), CallbackLoader.this);
                    }
                    @Override
                    public void onError(Throwable t) {
                        mCallback.failure(t);
                    }
                    @Override
                    public void onComplete() {
                        //
                    }
                }));
    }
}
