package com.turlir.abakgists.view.anim.creator;

import android.support.annotation.FloatRange;

/**
 * Набор дополнительных параметров анимации, не привязанных к конкретным view.
 */
public class Setting {

    /**
     * Задержка
     */
    public final long delay;
    /**
     * Продолжительность
     */
    public final long duration;
    /**
     * Прозрачность
     */
    public final float alpha;

    public Setting(long delay, long duration, @FloatRange(from = 0, to = 1) float alpha) {
        this.delay = delay;
        this.duration = duration;
        this.alpha = alpha;
    }
}
