package com.turlir.abakgists.view.anim.factor;

import android.view.View;

import com.turlir.abakgists.view.anim.base.AnimationCreator;
import com.turlir.abakgists.view.anim.base.Factory;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractFactoryOfThree
        extends Factory {

    private static final int LENGTH = 3;

    @Override
    protected List<AnimationCreator> create(View... views) {
        if (views == null || views.length != 3) {
            throw new IllegalArgumentException();
        }

        List<AnimationCreator> res = new ArrayList<>(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            switch (i) {
                case 0:
                    res.add(first(views[i]));
                    break;
                case 1:
                    res.add(second(views[i]));
                    break;
                case 2:
                    res.add(three(views[i]));
                    break;
            }
        }
        return res;
    }

    protected abstract AnimationCreator first(View v);

    protected abstract AnimationCreator second(View v);

    protected abstract AnimationCreator three(View v);
}
