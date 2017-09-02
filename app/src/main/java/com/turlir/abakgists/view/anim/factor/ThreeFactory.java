package com.turlir.abakgists.view.anim.factor;

import android.view.View;

import com.turlir.abakgists.view.anim.base.AnimationCreator;
import com.turlir.abakgists.view.anim.creator.Setting;
import com.turlir.abakgists.view.anim.creator.VerticalMove;

public class ThreeFactory
        extends AbstractFactoryOfThree {

    private final Setting mSetting;

    public ThreeFactory(Setting setting) {
        mSetting = setting;
    }

    @Override
    public AnimationCreator first(View v) {
        return new VerticalMove(mSetting, -v.getHeight());
    }

    @Override
    public AnimationCreator second(View v) {
        return null;
    }

    @Override
    public AnimationCreator three(View v) {
        return new VerticalMove(mSetting, v.getHeight());
    }
}
