package com.turlir.abakgists.base.loader.callback;

import com.turlir.abakgists.base.loader.Window;

import java.util.List;

public interface DataCallback<T> {

    void content(List<T> items, Window w, CallbackLoader<T> parent);

    void failure(Throwable t);
}
