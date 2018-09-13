package com.turlir.abakgists.base.loader.callback;

import com.turlir.abakgists.base.loader.Window;

import java.util.List;

class EmptyCallback<T> implements DataCallback<T> {

    @Override
    public void content(List<T> items, Window w, CallbackLoader<T> parent) {

    }
    @Override
    public void failure(Throwable t) {
        //
    }
}
