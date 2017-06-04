package com.turlir.abakgists.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.turlir.abakgists.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class SwitchLayout extends FrameLayout implements Switching {

    private static final int MAX_CHILD = 3;

    public static final int
            CONTENT = 0,
            ERROR = 1,
            LOADING = 1 << 1;

    private int mIndex;

    public SwitchLayout(@NonNull Context context) {
        this(context, null);
    }

    public SwitchLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SwitchLayout, 0, 0);
        try {
            mIndex = ta.getInteger(R.styleable.SwitchLayout_init, LOADING); // default
        } finally {
            ta.recycle();
        }
    }

    @Override
    public void toContent() {
        changeGroup(CONTENT);
    }

    @Override
    public void toError() {
        changeGroup(ERROR);
    }

    @Override
    public void toLoading() {
        changeGroup(LOADING);
    }

    @Override
    @Group
    public int currentGroup() {
        return mIndex;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > MAX_CHILD) {
            throw new IllegalStateException("getChildCount() must be between 0 and 2");
        }
        super.addView(child, index, params);
        changeGroup(mIndex);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new SwitchLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new SwitchLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return generateDefaultLayoutParams();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable instance = super.onSaveInstanceState();
        return new SwitchLayoutState(instance, this);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        SwitchLayoutState now = (SwitchLayoutState) state;
        now.apply(this);
        changeGroup(mIndex);
    }

    /**
     * optimize
     */
    private int changeGroup(int i) {
        if (i < getChildCount()) {

            View old = getChildAt(i);
            old.setVisibility(View.VISIBLE);

            if (i != mIndex) {
                hideChild(mIndex);

            } else if (getChildCount() > 1) {
                int t = getChildCount() - 1;
                if (t != i) hideChild(t);
            }

        } else {
            hideChild(getChildCount() - 1);
        }

        return mIndex = i;
    }

    private void hideChild(int i) {
        View child = getChildAt(i);
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        int def = ((SwitchLayoutParams) lp).getHideVisibility();
        //noinspection WrongConstant
        child.setVisibility(def);
    }

    private static class SwitchLayoutParams extends FrameLayout.LayoutParams {

        private final int mHided;

        SwitchLayoutParams(int width, int height) {
            super(width, height);
            mHided = View.INVISIBLE;
        }

        SwitchLayoutParams(@NonNull Context c, @Nullable AttributeSet attrs) {
            super(c, attrs);

            TypedArray ta = c.obtainStyledAttributes(attrs, R.styleable.SwitchLayout_Layout, 0, 0);
            try {
                mHided = ta.getInteger(R.styleable.SwitchLayout_Layout_hided, View.INVISIBLE); // default
            } finally {
                ta.recycle();
            }
        }

        int getHideVisibility() {
            return mHided;
        }

    }

    private static final class SwitchLayoutState extends BaseSavedState {

        public static final Parcelable.Creator<SwitchLayoutState> CREATOR
                = new Parcelable.Creator<SwitchLayoutState>() {

            public SwitchLayoutState createFromParcel(Parcel in) {
                return new SwitchLayoutState(in);
            }

            public SwitchLayoutState[] newArray(int size) {
                return new SwitchLayoutState[size];
            }
        };

        int index;

        private SwitchLayoutState(Parcelable in, SwitchLayout layout) {
            super(in);
            index = layout.mIndex;
        }

        private SwitchLayoutState(Parcel source) {
            super(source);
            index = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(index);
        }

        private void apply(SwitchLayout layout) {
            layout.mIndex = index;
        }

    }

    @IntDef(flag = true, value = {
            CONTENT, ERROR, LOADING
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface Group {
    }

}
