package com.turlir.abakgists.gistsloader;

import android.support.annotation.Nullable;

import com.turlir.abakgists.base.loader.Window;

public class Accumulator {

    private int accumulator;

    @Nullable
    private Window last;
    private int lastAddition;

    public int now(Window now, int size) {
        if (last == null) {
            last = now;
            accumulator = size;
            lastAddition = size;
            return accumulator();
        }
        boolean notOverlap = size >= lastAddition;
        if (axis(now) > axis(last) && notOverlap) {
            accumulator += size;
            lastAddition = size;
            last = now;
        }
        if (axis(now) < axis(last)) {
            accumulator -= lastAddition;
            lastAddition = size;
            last = now;
        }
        if (axis(now) == axis(last)) {
            accumulator = accumulator - lastAddition + size;
            lastAddition = size;
            last = now;
        }
        return accumulator();
    }

    public int accumulator() {
        return accumulator;
    }

    private static int axis(Window w) {
        return w.start();
    }
}
