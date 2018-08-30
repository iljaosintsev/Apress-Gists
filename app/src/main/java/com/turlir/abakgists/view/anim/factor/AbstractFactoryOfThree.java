package com.turlir.abakgists.view.anim.factor;

import android.view.View;

import com.turlir.abakgists.view.anim.base.AnimationCreator;
import com.turlir.abakgists.view.anim.base.Factory;

import java.util.ArrayList;
import java.util.List;

/**
 * Фабрика для создания анимации для каждой из трех view.
 * Анимация представляется в виде делегата {@link AnimationCreator}.
 */
abstract class AbstractFactoryOfThree extends Factory {

    private static final int LENGTH = 3;

    @Override
    protected List<AnimationCreator> create(View... views) {
        if (views == null || views.length != LENGTH) {
            throw new IllegalArgumentException();
        }
        List<AnimationCreator> res = new ArrayList<>(LENGTH);
        res.add(first(views[0]));
        res.add(second(views[1]));
        res.add(three(views[2]));
        return res;
    }

    /**
     * @param v первая view
     * @return делегат для анимирования первой view
     */
    protected abstract AnimationCreator first(View v);

    /**
     * @param v вторая view
     * @return делегат для анимирования второй view
     */
    protected abstract AnimationCreator second(View v);

    /**
     * @param v третья view
     * @return делегат для анимирования третьей view
     */
    protected abstract AnimationCreator three(View v);
}
