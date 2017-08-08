package com.turlir.abakgists.view.anim.factor;

import android.view.View;

import com.turlir.abakgists.view.anim.base.AnimationCreator;
import com.turlir.abakgists.view.anim.base.Factory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSpecialFactory
        extends Factory {

    private final int mSpecial;

    AbstractSpecialFactory(int special) {
        mSpecial = special;
    }

    @Override
    protected List<AnimationCreator> create(View... views) {
        if (views == null || views.length < 1) {
            throw new IllegalArgumentException();
        }
        List<AnimationCreator> list = new ArrayList<>(views.length);
        for (int i = 0; i < views.length; i++) {
            View view = views[i];
            if (i == mSpecial) {
                list.add(special(view));
            } else {
                list.add(standard(view));
            }
        }
        return list;
    }

    protected abstract AnimationCreator special(View v);

    protected abstract AnimationCreator standard(View v);
}
