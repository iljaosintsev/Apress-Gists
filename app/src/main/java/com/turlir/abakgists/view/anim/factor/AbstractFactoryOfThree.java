package com.turlir.abakgists.view.anim.factor;

import android.view.View;

import com.turlir.abakgists.view.anim.base.AnimationCreator;
import com.turlir.abakgists.view.anim.base.Factory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFactoryOfThree
        extends Factory {

    @Override
    protected List<AnimationCreator> create(View... views) {
        if (views.length > 3) {
            throw new IllegalArgumentException();
        }

        List<AnimationCreator> res = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            if (views.length > i) {
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
            } else {
                break;
            }
        }
        return res;
    }

    public abstract AnimationCreator first(View v);

    public abstract AnimationCreator second(View v);

    public abstract AnimationCreator three(View v);
}
