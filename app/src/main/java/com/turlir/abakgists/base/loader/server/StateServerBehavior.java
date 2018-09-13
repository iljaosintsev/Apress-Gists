package com.turlir.abakgists.base.loader.server;

import com.turlir.abakgists.base.loader.Window;

import java.util.List;

public interface StateServerBehavior<T> {

    boolean shouldRequest(List<T> nextItems, Window window);

    boolean shouldRender(List<T> items, Window window);

    void afterRender(List<T> last);

    boolean lastEqual(T last);
}
