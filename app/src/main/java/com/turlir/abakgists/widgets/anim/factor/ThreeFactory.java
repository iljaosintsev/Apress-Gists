package com.turlir.abakgists.widgets.anim.factor;

import android.view.View;

import com.turlir.abakgists.widgets.anim.base.AnimationCreator;
import com.turlir.abakgists.widgets.anim.creator.Setting;
import com.turlir.abakgists.widgets.anim.creator.VerticalMove;

/**
 * Создает делегаты однонаправленной анимации горизонтального движения для двух из трех view.
 * Анимация для второй {@link #second(View)} view равна {@code null}.
 * Характер анимации: вторая view остается на месте, первая и вторая перемещаются в противоположные стороны и исчезают.
 */
public class ThreeFactory extends AbstractFactoryOfThree {

    private final Setting mSetting;

    /**
     * @param setting общие параметры анимации для всех трех view
     */
    public ThreeFactory(Setting setting) {
        mSetting = setting;
    }

    @Override
    public AnimationCreator first(View v) {
        return new VerticalMove(mSetting, -v.getHeight(), 0f);
    }

    @Override
    public AnimationCreator second(View v) {
        return null;
    }

    @Override
    public AnimationCreator three(View v) {
        return new VerticalMove(mSetting, v.getHeight(), 0f);
    }
}
