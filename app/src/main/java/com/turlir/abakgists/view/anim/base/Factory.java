package com.turlir.abakgists.view.anim.base;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import java.util.List;

/**
 * Фабрика {@link AnimatorSet} для нескольких view.
 * Количество экземпляров {@link AnimationCreator} должно соответствовать количеству view,
 * но любой из них может отсутствовать. В этом случае анимация для соответствующей view не будет включена
 * в {@link AnimatorSet}
 */
public abstract class Factory {

    /**
     * Создает набор настроенных и сгруппированных анимаций, пригодных к запуску
     * @param views целевые view
     * @return набор из нескольких групп анимаций
     */
    public final AnimatorSet animate(View... views) {
        final AnimatorSet set = new AnimatorSet();
        AnimatorSet.Builder builder = null;

        List<AnimationCreator> creators = create(views);
        if (creators.size() != views.length) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < creators.size(); i++) {
            AnimationCreator creator = creators.get(i);
            if (creator == null) {
                continue;
            }
            View v = views[i];
            ObjectAnimator a = creator.createAnimation(v);
            creator.settingAnimation(a);
            if (builder == null) {
                builder = set.play(a);
            } else {
                creator.grouping(builder, a);
            }
        }
        return set;
    }

    /**
     * Связавает экземляры view с параметрами их анимации
     * @param views целевые view
     * @return список делегатов, формирующих {@link ObjectAnimator} отдельно для каждой view
     */
    protected abstract List<AnimationCreator> create(View... views);
}
