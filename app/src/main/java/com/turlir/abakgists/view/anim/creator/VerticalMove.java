package com.turlir.abakgists.view.anim.creator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

import com.turlir.abakgists.view.anim.base.AnimationCreator;

public class VerticalMove
        implements AnimationCreator {

    private final Setting mSetting;
    private float mDistance;

    public VerticalMove(Setting setting, View v) {
        this(setting, v.getHeight());
    }

    public VerticalMove(Setting setting, float distance) {
        this.mSetting = setting;
        mDistance = distance;
    }

    @Override
    public ObjectAnimator createAnimation(View view) {
        PropertyValuesHolder t = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, mDistance);
        PropertyValuesHolder a = PropertyValuesHolder.ofFloat(View.ALPHA, mSetting.alpha);
        return ObjectAnimator.ofPropertyValuesHolder(view, t, a);
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
