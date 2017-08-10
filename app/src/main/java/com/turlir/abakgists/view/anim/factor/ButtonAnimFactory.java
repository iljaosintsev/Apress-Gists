package com.turlir.abakgists.view.anim.factor;

import android.view.View;

import com.turlir.abakgists.view.anim.base.AnimationCreator;
import com.turlir.abakgists.view.anim.creator.Setting;
import com.turlir.abakgists.view.anim.creator.VerticalMove;

public class ButtonAnimFactory
        extends AbstractFactoryOfThree {

    private Params mParams;
    private Setting mCommon;

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
