package com.turlir.abakgists.widgets.anim.base;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Инкапсулирует работу с анимацией в виде {@link ObjectAnimator} для одной view.
 * Этапы жизненного цикла:
 * <ul>
 * <li>Создание</li>
 * <li>Настройка</li>
 * <li>Группировка</li>
 * </ul>
 */
public interface AnimationCreator {

    /**
     * Создание анимации для view
     * @param view целевая view
     * @return анимация
     */
    ObjectAnimator createAnimation(View view);

    /**
     * Настройка параметров анимации
     * @param view анимация
     */
    void settingAnimation(ObjectAnimator view);

    /**
     * Группировка или связывание нескольких анимаций
     * @param builder набор анимаций, куда необходимо прикрепить текущую
     * @param animator экземпляр анимации
     */
    void grouping(AnimatorSet.Builder builder, ObjectAnimator animator);
}
