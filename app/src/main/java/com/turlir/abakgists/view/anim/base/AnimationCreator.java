package com.turlir.abakgists.view.anim.base;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public interface AnimationCreator {

    ObjectAnimator createAnimation(View view);

    void settingAnimation(ObjectAnimator view);

    void grouping(AnimatorSet.Builder builder, ObjectAnimator animator);
}
