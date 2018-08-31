package com.turlir.abakgists.widgets.anim.factor;

import android.view.View;

import com.turlir.abakgists.widgets.anim.base.AnimationCreator;
import com.turlir.abakgists.widgets.anim.creator.Setting;
import com.turlir.abakgists.widgets.anim.creator.VerticalMove;

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
        return new VerticalMove(mCommon, mParams.distance(0), mParams.alpha(0));
    }

    @Override
    public AnimationCreator second(View v) {
        return new VerticalMove(mCommon, mParams.distance(1), mParams.alpha(1));
    }

    @Override
    public AnimationCreator three(View v) {
        return new VerticalMove(mCommon, mParams.distance(2), mParams.alpha(2));
    }
}
