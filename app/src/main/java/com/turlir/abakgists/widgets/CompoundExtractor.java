package com.turlir.abakgists.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;

import com.turlir.abakgists.R;

abstract class CompoundExtractor {

    static Drawable[] extract(Context context, AttributeSet attrs, Drawable[] pd) {

        for (Drawable drawable : pd) {
            if (drawable != null) {
                throw new UnsupportedOperationException("do not use sdk compound drawable attributes");
            }
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VectorButton);
        try {

            Drawable leftDrawable = getDrawable(context, ta, R.styleable.VectorButton_drawableLeftCompat);
            Drawable rightDrawable = getDrawable(context, ta, R.styleable.VectorButton_drawableRightCompat);
            Drawable topDrawable = getDrawable(context, ta, R.styleable.VectorButton_drawableTopCompat);
            Drawable bottomDrawable = getDrawable(context, ta, R.styleable.VectorButton_drawableBottomCompat);

            return new Drawable[] {
                    leftDrawable, topDrawable, rightDrawable, bottomDrawable
            };

        } finally {
            ta.recycle();
        }
    }

    private static Drawable getDrawable(Context context, TypedArray ta, int id) {
        int leftId = ta.getResourceId(id, -1);
        if (leftId != -1) {
            return VectorDrawableCompat.create(context.getResources(), leftId, context.getTheme());
        }
        return null;
    }
}
