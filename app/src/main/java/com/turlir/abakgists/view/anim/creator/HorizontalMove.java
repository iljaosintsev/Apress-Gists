package com.turlir.abakgists.view.anim.creator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import com.turlir.abakgists.view.anim.base.AnimationCreator;

public class HorizontalMove
        implements AnimationCreator {

    private final Setting mSetting;
    private final float mDistance;

    public HorizontalMove(Setting setting, View v) {
        this(setting, v.getWidth());
    }

    public HorizontalMove(Setting setting, float distance) {
        this.mSetting = setting;
        mDistance = distance;
    }

    @Override
    public ObjectAnimator createAnimation(View view) {
        return ObjectAnimator
                .ofFloat(view, View.TRANSLATION_X, mDistance);
    }

    @Override
    public void settingAnimation(ObjectAnimator view) {
        view.setDuration(mSetting.duration).setStartDelay(mSetting.delay);
    }

    @Override
    public void grouping(AnimatorSet.Builder builder, ObjectAnimator animator) {
        builder.with(animator);
    }

}
