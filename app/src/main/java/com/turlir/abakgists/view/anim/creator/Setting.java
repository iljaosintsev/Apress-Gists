package com.turlir.abakgists.view.anim.creator;

import android.support.annotation.FloatRange;

public class Setting {

    public final long delay, duration;
    public final float alpha;

    public Setting(long delay, long duration, @FloatRange(from = 0, to = 1) float alpha) {
        this.delay = delay;
        this.duration = duration;
        this.alpha = alpha;
    }

    public Setting(long delay, long duration) {
        this.delay = delay;
        this.duration = duration;
        alpha = -1;
    }

    public boolean shouldAlphaChange() {
        return alpha == -1;
    }

}
