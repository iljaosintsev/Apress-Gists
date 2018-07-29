package com.turlir.abakgists.api;

import io.reactivex.functions.Consumer;

/**
 * Эффект задержки сети на {@code millis} миллисекунд
 */
class LagSideEffect implements Consumer<Object> {

    private final int mLag;

    LagSideEffect(int millis) {
        mLag = millis;
    }

    @Override
    public void accept(Object o) {
        try {
            Thread.sleep(mLag);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
