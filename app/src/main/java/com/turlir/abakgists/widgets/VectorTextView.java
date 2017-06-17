package com.turlir.abakgists.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class VectorTextView extends AppCompatTextView {

    public VectorTextView(Context context) {
        this(context, null);
    }

    public VectorTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VectorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Drawable[] pd = VectorDrawableFactory.extractCompoundDrawable(context, attrs, getCompoundDrawables());
        setCompoundDrawablesWithIntrinsicBounds(pd[0], pd[1], pd[2], pd[3]);
    }
}
