package com.turlir.abakgists.api;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Эффект задержки сети на {@code millis} миллисекунд
 */
class LagSideEffect implements Consumer<Object> {

    private final long mLag;

    LagSideEffect() {
        this(TimeUnit.SECONDS.toMillis(6));
    }

    LagSideEffect(long millis) {
        mLag = millis;
    }

    @Override
    public void accept(Object o) {
        try {
            Thread.sleep(mLag);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
