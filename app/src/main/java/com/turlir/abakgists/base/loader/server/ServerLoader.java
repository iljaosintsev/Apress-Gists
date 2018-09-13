package com.turlir.abakgists.base.loader.server;

import android.support.annotation.NonNull;

import com.turlir.abakgists.base.loader.Window;
import com.turlir.abakgists.base.loader.callback.CallbackLoader;
import com.turlir.abakgists.base.loader.callback.DataCallback;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ServerLoader<T, V> {

    private final CallbackLoader<T> mDelegate;
    private final Serverable mServer;
    private final CompositeDisposable mResource;

    private ServerCallback<T, V> mCallback;

    public ServerLoader(CallbackLoader<T> delegate, Serverable connection) {
        mDelegate = delegate;
        mServer = connection;
        mResource = new CompositeDisposable();
    }

    public final void setCallback(@NonNull ServerCallback<T, V> callback) {
        mCallback = callback;
        mDelegate.setCallback(new DataCallback<T>() {
            @Override
            public void content(List<T> items, Window w, CallbackLoader<T> parent) {
                callback.content(items, w, ServerLoader.this);
            }
            @Override
            public void failure(Throwable t) {
                callback.failure(t);
            }
        });
    }

    public final void currentPage() {
        mDelegate.currentPage();
    }

    public final void nextPage() {
        mDelegate.nextPage();
    }

    public final void prevPage() {
        mDelegate.prevPage();
    }

    public final void loadFromServer(LoadableItem page) {
        mResource.add(mServer.loadFromServer(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onLoadedFromServer,
                        t -> mDelegate.getCallback().failure(t)
                )
        );
    }

    public final void update(Window w) {
        onPreUpdate(w);
        stop();
        LoadableItem page = w.constraint(w.count());
        mResource.add(mServer.update(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(c -> mDelegate.setWindow(w))
                .subscribe(
                        c -> mDelegate.currentPage(),
                        t -> mDelegate.getCallback().failure(t)
                )
        );
    }

    public final Window getWindow() {
        return mDelegate.getWindow();
    }

    public final void stop() {
        mResource.clear();
        mDelegate.stop();
    }

    private void onLoadedFromServer(int count) {
        mCallback.onLoadedFromServer(count);
    }

    private void onPreUpdate(Window w) {
        mCallback.onPreUpdate(w);
    }
}
