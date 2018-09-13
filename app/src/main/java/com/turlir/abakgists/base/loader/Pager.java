package com.turlir.abakgists.base.loader;

import java.util.List;

import io.reactivex.Flowable;

public interface Pager<T> {

    Flowable<List<T>> currentPage(Window w);
}
