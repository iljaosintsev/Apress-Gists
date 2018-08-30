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
 * Минимальное значение перемещения передается в виде аргумента {@code value}
 * в метод {@link #distance(int position, float value)}.
 * Итоговое перемещение вычисляется по формуле {@code v * m + a}, где
 * <ul>
 *     <li>v – минимальное значение перемещения</li>
 *     <li>m – направления перемещения (1 – вниз, минус 1 – вверх, 0 – на месте)</li>
 *     <li>a – слагаемое для компенсации отступов между view</li>
 * </ul>
 */
public class Params {

    private final List<Integer> distanceMulti; // изменяет направление перемещения
    private final List<Float> distanceAdd; // компенсирует расстояние между view
    private final List<Float> alpha; // "исчезает" view

    private Params() {
        distanceMulti = new ArrayList<>(3);
        distanceAdd = new ArrayList<>(3);
        alpha = new ArrayList<>(3);
    }

    /**
     * @param position порядковый номер view
     * @return значение прозрачности
     */
    float alpha(int position) {
        return alpha.get(position);
    }

    /**
     * Рассчитывается изменение положения для view с учетом хранимого множителя и приращения
     * @param position порядковый номер view
     * @param value базовое значение перемещения
     * @return изменение положения для view
     */
    float distance(int position, float value) {
        return value * distanceMulti.get(position) + distanceAdd.get(position);
    }

    private Params multi(int arg) {
        distanceMulti.add(arg);
        return this;
    }

    private Params additional(float arg) {
        distanceAdd.add(arg);
        return this;
    }

    private Params alpha(@FloatRange(from = 0, to = 1) float arg) {
        alpha.add(arg);
        return this;
    }

    private boolean consistent() {
        int du = distanceMulti.size();
        int da = distanceAdd.size();
        int a = alpha.size();
        return du == da && da == a;
    }

    @Override
    public String toString() {
        return Arrays.toString(distanceMulti.toArray()) + "\n" +
               Arrays.toString(distanceAdd.toArray()) + "\n" +
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
                back.additional(0);

                if (item == mSelected) {
                    float distance = Math.abs(mSelected.getY() - mViews[1].getY()) - item.getHeight();
                    int sign = Float.compare(mViews[1].getY(), mSelected.getY());
                    there.additional(distance * sign);
                    there.multi(Float.compare(mViews[1].getY(), item.getY()));
                    back.multi(Float.compare(item.getY(), mViews[1].getY()) / 2);
                } else {
                    there.additional(0f);
                    there.multi(Float.compare(item.getY(), mSelected.getY()));
                    back.multi(Float.compare(mSelected.getY(), mViews[1].getY()) / 2);
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
                    params.additional(distance * sign); // двигаться
                    params.multi(Float.compare(mViews[1].getY(), item.getY())); // я относительно центра

                } else {
                    params.additional(0f); // дополнительно никуда не двигаться
                    params.multi(Float.compare(item.getY(), mSelected.getY())); // я относительно выбранного
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
