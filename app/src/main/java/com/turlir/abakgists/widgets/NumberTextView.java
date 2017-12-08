package com.turlir.abakgists.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;

import com.turlir.abakgists.R;

import butterknife.BindDimen;
import butterknife.ButterKnife;

public class NumberTextView extends android.support.v7.widget.AppCompatTextView {

    @BindDimen(R.dimen.number_tv_padding)
    int padding;

    public NumberTextView(Context context) {
        this(context, null);
    }

    public NumberTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ButterKnife.bind(this);

        setBackgroundResource(R.drawable.number_tv_bg);
        setGravity(Gravity.CENTER);
        setTextAppearance(getContext(), R.style.NumberTextAppearance);
        setPadding(padding, padding, padding, padding);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        if (w != h) {
            int max = Math.max(w, h);
            setMinimumWidth(max);
            setMinHeight(max);
            // setMeasuredDimension(max, max);
            // invalidate();
        }
    }


}
