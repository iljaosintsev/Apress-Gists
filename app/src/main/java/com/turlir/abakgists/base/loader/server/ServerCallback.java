package com.turlir.abakgists.base.loader.server;

import com.turlir.abakgists.base.loader.Window;

import java.util.List;

public interface ServerCallback<T, V> {

    void content(List<T> items, Window w, ServerLoader<T, V> parent);

    void failure(Throwable t);

    void onLoadedFromServer(int c);

    void onPreUpdate(Window w);
}
