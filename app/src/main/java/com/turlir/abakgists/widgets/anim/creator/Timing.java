package com.turlir.abakgists.widgets.anim.creator;

/**
 * Набор дополнительных параметров анимации, не привязанных к конкретным view.
 */
public class Timing {

    /**
     * Задержка
     */
    public final long delay;
    /**
     * Продолжительность
     */
    public final long duration;

    public Timing(long delay, long duration) {
        this.delay = delay;
        this.duration = duration;
    }
}
