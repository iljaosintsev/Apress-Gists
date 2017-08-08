package com.turlir.abakgists.view.anim.base;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import java.util.List;

public abstract class Factory {

    public final AnimatorSet animate(View... views) {
        final AnimatorSet set = new AnimatorSet();
        AnimatorSet.Builder builder = null;

        List<AnimationCreator> creators = create(views);
        for (int i = 0; i < creators.size(); i++) {
            AnimationCreator creator = creators.get(i);
            if (creator == null) continue;

            View v = views[i];

            ObjectAnimator a = creator.createAnimation(v);

            creator.settingAnimation(a);

            if (builder == null) {
                builder = set.play(a);
            } else {
                creator.grouping(builder, a);
            }
        }
        return set;
    }

    protected abstract List<AnimationCreator> create(View... views);
}
