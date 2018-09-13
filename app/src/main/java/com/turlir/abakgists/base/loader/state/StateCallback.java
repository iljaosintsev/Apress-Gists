package com.turlir.abakgists.base.loader.state;

import com.turlir.abakgists.base.loader.Window;
import com.turlir.abakgists.base.loader.server.StateServerBehavior;

import java.util.List;

public interface StateCallback<T, V> {

    void content(List<T> items, Window w, StateServerLoader<T, V> parent);

    void failure(Throwable t);

    void onLoadedFromServer(int c);

    void onPreUpdate(Window w);

    boolean canLoad();

    StateServerBehavior<T> getHelper();
}
