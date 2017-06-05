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

import timber.log.Timber;

public class SwitchLayout extends FrameLayout {

    private static final int MAX_CHILD = 3;

    public static final int
            CONTENT = 0,
            ERROR = 1,
            LOADING = 1 << 1;

    private int mIndex;

    private Setting mLastItem;

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
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > MAX_CHILD) {
            throw new IllegalStateException("getChildCount() must be between 0 and 2");
        }
        super.addView(child, index, params);

        final int i;
        if (index > 0) {
            i = index;
        } else {
            i = getChildCount() - 1;
        }
        applyGroupByChild(i);
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

    public void toContent() {
        changeGroup(CONTENT);
    }

    public void toError() {
        changeGroup(ERROR);
    }

    public void toLoading() {
        changeGroup(LOADING);
    }

    @Group
    public int currentGroup() {
        return mIndex;
    }

    /**
     * Применение группы к только-что добавленному потомку
     */
    private boolean applyGroupByChild(int child) {
        if (mLastItem != null) {

            if (child == mLastItem.getPosition()) {
                showChild(child);
                return true;
            } else {
                hideChild(child);
                return false;
            }

        } else {

            if (doesViewToGroup(mIndex, child)) { // mLastItem.getPosition() кэширует значение doesViewToGroup
                mLastItem = new Setting(mIndex, child);
                showChild(child);
                return true;
            } else {
                hideChild(child);
                return false;
            }

        }
    }

    /**
     * Изменение группы при наличии/отсутствии потомков
     */
    private int changeGroup(int group) {

        if (mLastItem != null) { // есть настройки

            if (group != mLastItem.getGroup()) { // группа изменилась
                hideChild(mLastItem.getPosition());

                int position = getChildIndexByGroup(group);
                mLastItem = new Setting(group, position);
                showChild(mLastItem.getPosition());
            }

            return mIndex = group; // иначе ничего не делаем

        } else { // нет настроек

            int index = getChildIndexByGroup(group);
            showChild(index);
            mLastItem = new Setting(group, index);

            return mIndex = group;
        }
    }

    private boolean doesViewToGroup(int group, int index) {
        return group == index;
    }

    private int getChildIndexByGroup(int group) {
        return group;
    }

    private void hideChild(int i) {
        Timber.i("hideChild %d", i);
        View child = getChildAt(i);
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        int def = ((SwitchLayoutParams) lp).getHideVisibility();
        //noinspection WrongConstant
        child.setVisibility(def);
    }

    private void showChild(int i) {
        Timber.i("showChild %d", i);
        getChildAt(i).setVisibility(View.VISIBLE);
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

    private static class Setting {

        private int mGroup;
        private int mPosition;

        Setting(int group, int position) {
            this.mGroup = group;
            this.mPosition = position;
        }

        private int getGroup() {
            return mGroup;
        }

        private int getPosition() {
            return mPosition;
        }

    }
}
