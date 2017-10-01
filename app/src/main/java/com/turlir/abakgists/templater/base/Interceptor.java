package com.turlir.abakgists.templater.base;

import android.view.View;

public interface Interceptor<T extends View> {

    void add(T view);
}
