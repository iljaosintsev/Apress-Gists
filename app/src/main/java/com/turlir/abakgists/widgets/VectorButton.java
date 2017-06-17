package com.turlir.abakgists.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class VectorButton extends AppCompatButton {

    public VectorButton(Context context) {
        this(context, null);
    }

    public VectorButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VectorButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Drawable[] pd = VectorDrawableFactory.extractCompoundDrawable(context, attrs, getCompoundDrawables());
        setCompoundDrawablesWithIntrinsicBounds(pd[0], pd[1], pd[2], pd[3]);
    }

}
