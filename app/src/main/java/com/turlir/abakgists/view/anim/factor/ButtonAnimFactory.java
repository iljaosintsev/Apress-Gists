package com.turlir.abakgists.view.anim.factor;

import android.view.View;

import com.turlir.abakgists.view.anim.base.AnimationCreator;
import com.turlir.abakgists.view.anim.creator.Setting;
import com.turlir.abakgists.view.anim.creator.VerticalMove;

/**
 * Создает делегаты однонаправленной анимации вертикального движения для набора из трех view.
 * Характер анимации: якорная view перемещается в центре композиции, две остальные отъезжают от нее и исчезают.
 */
public class ButtonAnimFactory extends AbstractFactoryOfThree {

    private final Params mParams;
    private final Setting mCommon;

    /**
     * @param specific спецификация анимации
     * @param common общие параметры
     */
    public ButtonAnimFactory(Params specific, Setting common) {
        mParams = specific;
        mCommon = common;
    }

    @Override
    public AnimationCreator first(View v) {
        Setting s = new Setting(mCommon.delay, mCommon.duration, mParams.alpha(0));
        return new VerticalMove(s, mParams.distance(0, v.getHeight()));
    }

    @Override
    public AnimationCreator second(View v) {
        Setting s = new Setting(mCommon.delay, mCommon.duration, mParams.alpha(1));
        return new VerticalMove(s, mParams.distance(1, v.getHeight()));
    }

    @Override
    public AnimationCreator three(View v) {
        Setting s = new Setting(mCommon.delay, mCommon.duration, mParams.alpha(2));
        return new VerticalMove(s, mParams.distance(2, v.getHeight()));
    }
}
