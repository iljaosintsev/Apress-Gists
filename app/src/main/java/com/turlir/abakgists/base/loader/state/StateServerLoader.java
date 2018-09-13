package com.turlir.abakgists.base.loader.state;

import com.turlir.abakgists.base.loader.Window;
import com.turlir.abakgists.base.loader.server.LoadableItem;
import com.turlir.abakgists.base.loader.server.ServerCallback;
import com.turlir.abakgists.base.loader.server.ServerLoader;

import java.util.List;

public class StateServerLoader<T, V> {

    private final ServerLoader<T, V> mDelegate;

    private StateCallback<T, V> mCallback;

    public StateServerLoader(ServerLoader<T, V> delegate) {
        mDelegate = delegate;
    }

    public void setCallback(StateCallback<T, V> callback) {
        mCallback = callback;
        mDelegate.setCallback(new ServerCallback<T, V>() {
            @Override
            public void content(List<T> items, Window w, ServerLoader<T, V> parent) {
                callback.content(items, w, StateServerLoader.this);
            }
            @Override
            public void failure(Throwable t) {
                callback.failure(t);
            }
            @Override
            public void onLoadedFromServer(int c) {
                callback.onLoadedFromServer(c);
            }
            @Override
            public void onPreUpdate(Window w) {
                callback.onPreUpdate(w);
            }
        });
    }

    //

    public final void currentPage() {
        mDelegate.currentPage();
    }

    public final void nextPage() {
        mDelegate.nextPage();
    }

    public final void prevPage() {
        mDelegate.prevPage();
    }

    public final void update(Window w) {
        mDelegate.update(w);
    }

    public void loadFromServer(LoadableItem page) {
        mDelegate.loadFromServer(page);
    }

    public Window getWindow() {
        return mDelegate.getWindow();
    }

    public void stop() {
        mDelegate.stop();
    }

    //

    public boolean hasLoad() {
        return mCallback.canLoad();
    }

    public boolean hasNext() {
        return hasLoad() && mDelegate.getWindow().hasNext();
    }

    public boolean hasPrevious() {
        return hasLoad() && mDelegate.getWindow().hasPrevious();
    }

    public boolean resetForward(T last) {
        return mCallback.getHelper().lastEqual(last) && hasNext();
    }

    public boolean resetBackward(T last) {
        return mCallback.getHelper().lastEqual(last) && hasPrevious();
    }
}
