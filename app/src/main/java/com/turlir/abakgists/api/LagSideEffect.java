package com.turlir.abakgists.api;

import rx.functions.Action1;

/**
 * Эффект задержки сети на {@code millis} миллисекунд
 */
class LagSideEffect implements Action1<Object> {

    private final int mLag;

    LagSideEffect(int millis) {
        mLag = millis;
    }

    @Override
    public void call(Object data) {
        try {
            Thread.sleep(mLag);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
