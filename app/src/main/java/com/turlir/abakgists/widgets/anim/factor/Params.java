package com.turlir.abakgists.widgets.anim.factor;

import android.support.annotation.FloatRange;
import android.support.v4.util.Pair;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Описывает параметры анимации для нескольких view
 * <ul>
 *     <li>Расстояние</li>
 *     <li>Показатель направления движения</li>
 *     <li>Прозрачность</li>
 * </ul>
 * Первые два параметра в совокупности представляют изменение положения view (на сколько {@code px} и в какую сторону).
 * Итоговое перемещение вычисляется по формуле {@code v * m + a}, где
 * <ul>
 *     <li>v – базовое значение перемещения (высота view)</li>
 *     <li>m – направления перемещения (1 – вниз, минус 1 – вверх, 0 – на месте)</li>
 *     <li>a – слагаемое для компенсации отступов между view</li>
 * </ul>
 */
public class Params {

    private final List<Float> distance; // изменяет направление перемещения
    private final List<Float> alpha; // "исчезает" view

    private Params() {
        distance = new ArrayList<>(3);
        alpha = new ArrayList<>(3);
    }

    /**
     * @param position порядковый номер view
     * @return значение прозрачности
     */
    float alpha(int position) {
        return alpha.get(position);
    }

    float distance(int position) {
        return distance.get(position);
    }

    private Params distance(int multi, float add, float value) {
        distance.add(value * multi + add);
        return this;
    }

    private Params alpha(@FloatRange(from = 0, to = 1) float arg) {
        alpha.add(arg);
        return this;
    }

    private boolean consistent() {
        return distance.size() == alpha.size();
    }

    @Override
    public String toString() {
        return Arrays.toString(distance.toArray()) + "\n" +
               Arrays.toString(alpha.toArray());
    }

    /**
     * Создает два экземпляра класса Params, описывающих анимацию трех view в двух направлениях. <p>
     * Конструктор принимает ссылку на якорную view, методы {@link #up}, {@link #center} и {@link #down} задают
     * местоположение view друг относительно друга. <p>
     * Параметры анимации рассчитываются с учетом положения view друг относительно друга.
     * Прозрачность якорной view приравнивается к 1, остальных – к 0. Смещение якорной view равно
     * расстоянию от нее до центральной view. Остальные view перемещаются на величину свой высоты в противоположную
     * от якорной view сторону. Таким образом якорная view не пересекается с периферийными. <p>
     * Второй набор параметров описывают обратную анимацию.
     */
    public static class TwoWayBuilder {

        private View mSelected;
        private View[] mViews;

        /**
         * @param selected якорная view
         */
        public TwoWayBuilder(View selected) {
            mSelected = selected;
            mViews = new View[3];
        }

        /**
         * @param v view, которая находится выше остальных
         * @return Builder fluent-interface
         */
        public TwoWayBuilder up(View v) {
            mViews[0] = v;
            return this;
        }

        /**
         * @param v view, которая находится в центре
         * @return Builder fluent-interface
         */
        public TwoWayBuilder center(View v) {
            mViews[1] = v;
            return this;
        }

        /**
         * @param v view, которая находится ниже остальных
         * @return Builder fluent-interface
         */
        public TwoWayBuilder down(View v) {
            mViews[2] = v;
            return this;
        }

        /**
         * Создает параметры двунаправленной анимации для трех view
         * @return параметры прямой анимации в {@link Pair#first}, обратной – {@link Pair#second}
         */
        public Pair<Params, Params> compute() {
            if (mSelected == null || mViews == null) throw new IllegalArgumentException();
            for (View item : mViews) if (item == null) throw new IllegalArgumentException();

            Params there = new Params(),
                    back = new Params();

            for (View item : mViews) {
                there.alpha(mSelected == item ? 1 : 0f);
                back.alpha(1f);

                if (item == mSelected) {
                    float distance = Math.abs(mSelected.getY() - mViews[1].getY()) - item.getHeight();
                    int sign = Float.compare(mViews[1].getY(), mSelected.getY());
                    float tadd = distance * sign;
                    int tm = Float.compare(mViews[1].getY(), item.getY());
                    there.distance(tm, tadd, item.getHeight());

                    int bm = Float.compare(item.getY(), mViews[1].getY()) / 2;
                    back.distance(bm, 0f, item.getHeight());

                } else {
                    int tm = Float.compare(item.getY(), mSelected.getY());
                    there.distance(tm, 0f, item.getHeight());

                    int bm = Float.compare(mSelected.getY(), mViews[1].getY()) / 2;
                    back.distance(bm, 0f, item.getHeight());
                }
            }

            mSelected = null;
            mViews = null;
            if (!there.consistent() || !back.consistent()) {
                throw new IllegalStateException();
            }
            return new Pair<>(there, back);
        }
    }

    /**
     * Создает один экземпляр класса Params, описывающий анимацию трех view в одном направлении. <p>
     * Конструктор принимает ссылку на якорную view, методы {@link #up}, {@link #center} и {@link #down} задают
     * местоположение view друг относительно друга. <p>
     * Параметры анимации рассчитываются с учетом положения view друг относительно друга.
     * Прозрачность якорной view приравнивается к 1, остальных – к 0. Смещение якорной view равно
     * расстоянию от нее до центральной view. Остальные view перемещаются на величину свой высоты в противоположную
     * от якорной view сторону. Таким образом якорная view не пересекается с периферийными.
     */
    public static class Builder {

        private View mSelected;
        private View[] mViews;

        /**
         * @param selected якорная view
         */
        public Builder(View selected) {
            mSelected = selected;
            mViews = new View[3];
        }

        /**
         * @param v view, которая находится выше остальных
         * @return Builder fluent-interface
         */
        public Builder up(View v) {
            mViews[0] = v;
            return this;
        }

        /**
         * @param v view, которая находится в центре
         * @return Builder fluent-interface
         */
        public Builder center(View v) {
            mViews[1] = v;
            return this;
        }

        /**
         * @param v view, которая находится ниже остальных
         * @return Builder fluent-interface
         */
        public Builder down(View v) {
            mViews[2] = v;
            return this;
        }

        /**
         * Создает параметры однонаправленной анимации для трех view
         * @return параметры анимации в объекте {@link Params}
         */
        public Params compute() {
            if (mSelected == null || mViews == null) throw new IllegalArgumentException();
            for (View item : mViews) if (item == null) throw new IllegalArgumentException();

            Params params = new Params();
            for (View item : mViews) {
                params.alpha(mSelected == item ? 1 : 0f);
                if (item == mSelected) { // я выбранный
                    float distance = Math.abs(mSelected.getY() - mViews[1].getY()) - item.getHeight(); // на сколько
                    int sign = Float.compare(mViews[1].getY(), mSelected.getY()); //  в какую сторону
                    float add = (distance * sign); // двигаться
                    int multi = Float.compare(mViews[1].getY(), item.getY()); // я относительно центра
                    params.distance(multi, add, item.getHeight());

                } else {
                    float add = 0f; // дополнительно никуда не двигаться
                    int multi = Float.compare(item.getY(), mSelected.getY()); // я относительно выбранного
                    params.distance(multi, add, item.getHeight());
                }
            }
            mSelected = null;
            mViews = null;
            if (!params.consistent()) {
                throw new IllegalStateException();
            }
            return params;
        }
    }

}
