package com.turlir.abakgists.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;

import com.turlir.abakgists.R;

public abstract class VectorDrawableFactory {

    private static final int[] IDS = new int[] {
            R.styleable.VectorButton_drawableLeftCompat,
            R.styleable.VectorButton_animateDrawableLeftCompat,

            R.styleable.VectorButton_drawableTopCompat,
            R.styleable.VectorButton_animateDrawableTopCompat,

            R.styleable.VectorButton_drawableRightCompat,
            R.styleable.VectorButton_animateDrawableRightCompat,

            R.styleable.VectorButton_drawableBottomCompat,
            R.styleable.VectorButton_animateDrawableBottomCompat
    };

    public static Drawable createDrawable(Context cnt, @DrawableRes int id) {
        return VectorDrawableCompat.create(cnt.getResources(), id, cnt.getTheme());
    }

    public static Drawable createAnimateDrawable(Context cnt, @DrawableRes int id) {
        return AnimatedVectorDrawableCompat.create(cnt, id);
    }

    static Drawable[] extractCompoundDrawable(Context context, AttributeSet attrs, Drawable[] pd) {
        for (Drawable drawable : pd) {
            if (drawable != null) {
                throw new UnsupportedOperationException("do not use sdk compound drawable attributes");
            }
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VectorButton);
        Drawable[] dr = new Drawable[4];
        try {
            for (int i = 0; i < IDS.length; i+=2) {
                int f = IDS[i];
                int s = IDS[i + 1];
                dr[i / 2] = getDrawable(context, ta, f, s);
            }
            return dr;
        } finally {
            ta.recycle();
        }
    }

    private static Drawable getDrawable(Context context, TypedArray ta, int std, int animate) {
        int stdRes = ta.getResourceId(std, -1);
        int animRes = ta.getResourceId(animate, -1);
        if (stdRes != -1 && animRes != -1) {
            throw new IllegalStateException("drawableCompat and animateDrawableCompat attr at the same time");
        }
        if (stdRes != -1) {
            return createDrawable(context, stdRes);
        } else if(animRes != -1) {
            return createAnimateDrawable(context, animRes);
        } else {
            return null;
        }
    }
}