package com.turlir.abakgists.gist;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class GistDeleteBus {

    private final PublishSubject<String> mBus;

    public GistDeleteBus() {
        mBus = PublishSubject.create();
    }

    public void gistDeleted(String id) {
        mBus.onNext(id);
    }

    public Disposable subscribe(Consumer<String> consumer) {
        return mBus.subscribe(consumer);
    }
}
