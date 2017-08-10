package com.turlir.abakgists.view.anim.factor;

import android.support.annotation.FloatRange;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Params {

    private final List<Integer> distanceMulti;
    private final List<Float> distanceAdd;
    private final List<Float> alpha;

    private Params() {
        distanceMulti = new ArrayList<>(3);
        distanceAdd = new ArrayList<>(3);
        alpha = new ArrayList<>(3);
    }

    private Params multi(Integer arg) {
        distanceMulti.add(arg);
        return this;
    }

    private Params additional(Float arg) {
        distanceAdd.add(arg);
        return this;
    }

    private Params alpha(@FloatRange(from = 0, to = 1) Float arg) {
        alpha.add(arg);
        return this;
    }

    float alpha(int position) {
        return alpha.get(position);
    }

    float distance(int position, float value) {
        return value * distanceMulti.get(position) + distanceAdd.get(position);
    }

    public static class Builder {

        private View mSelected;
        private View[] mViews;

        public Builder(View selected) {
            mSelected = selected;
            mViews = new View[3];
        }

        public Builder up(View v) {
            mViews[0] = v;
            return this;
        }

        public Builder center(View v) {
            mViews[1] = v;
            return this;
        }

        public Builder down(View v) {
            mViews[2] = v;
            return this;
        }

        public Params compute() {
            if (mSelected == null || mViews == null) throw new IllegalArgumentException();
            for (View item : mViews) if (item == null) throw new IllegalArgumentException();

            Params params = new Params();
            for (View item : mViews) {
                params.alpha(mSelected == item ? 1 : 0f);
                if (item == mSelected) { // я выбранный
                    float distance = Math.abs(mSelected.getY() - mViews[1].getY()) - item.getHeight(); // на сколько
                    int sign = compare(mViews[1].getY(), mSelected.getY()); //  в какую сторону
                    params.additional(distance * sign); // двигаться

                    params.multi(compare(mViews[1].getY(), item.getY())); // я относительно центра
                } else {
                    params.additional(0f); // дополнительно никуда не двигаться

                    params.multi(compare(item.getY(), mSelected.getY())); // я относительно выбранного
                }
            }
            mSelected = null;
            mViews = null;
            return params;
        }

        private int compare(float a, float b) {
            return Float.valueOf(a).compareTo(b);
        }
    }

}
