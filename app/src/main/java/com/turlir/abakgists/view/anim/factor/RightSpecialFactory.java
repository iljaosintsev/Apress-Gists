package com.turlir.abakgists.view.anim.factor;

import android.support.annotation.IntRange;
import android.view.View;

import com.turlir.abakgists.view.anim.base.AnimationCreator;
import com.turlir.abakgists.view.anim.creator.HorizontalMove;
import com.turlir.abakgists.view.anim.creator.Setting;

public class RightSpecialFactory
        extends AbstractSpecialFactory {

    private final long mDelay;
    private final long mDuration;

    public RightSpecialFactory(long delay, long duration, @IntRange(from = 0, to = 2) int special) {
        super(special);
        mDelay = delay;
        mDuration = duration;
    }

    @Override
    protected AnimationCreator special(View v) {
        return new HorizontalMove(new Setting(mDelay, mDuration - mDelay, 0), v.getWidth());
    }

    @Override
    protected AnimationCreator standard(View v) {
        return new HorizontalMove(new Setting(0, mDuration, 0), v.getWidth());
    }
}
