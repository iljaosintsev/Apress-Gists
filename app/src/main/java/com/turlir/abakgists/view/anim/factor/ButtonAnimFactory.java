package com.turlir.abakgists.view.anim.factor;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
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
        Setting s = new Setting(mCommon.delay, mCommon.duration, mParams.alpha[0]);
        return new VerticalMove(s, mParams.distance(0, v.getHeight()));
    }

    @Override
    public AnimationCreator second(View v) {
        Setting s = new Setting(mCommon.delay, mCommon.duration, mParams.alpha[1]);
        return new VerticalMove(s, mParams.distance(1, v.getHeight()));
    }

    @Override
    public AnimationCreator three(View v) {
        Setting s = new Setting(mCommon.delay, mCommon.duration, mParams.alpha[2]);
        return new VerticalMove(s, mParams.distance(2, v.getHeight()));
    }

    public static class Params {

        private int[] distanceMulti;
        private float[] distanceAdd;
        private float[] alpha;

        public Params() {

        }

        public Params multi(@IntRange(from = -1, to = 1) int... arg) {
            distanceMulti = arg;
            return this;
        }

        public Params addit(float... arg) {
            distanceAdd = arg;
            return this;
        }

        public Params alpha(@FloatRange(from = 0, to = 1) float... arg) {
            alpha = arg;
            return this;
        }

        public float distance(int position, float value) {
            return value * distanceMulti[position] + distanceAdd[position];
        }

    }

}
