package com.turlir.abakgists.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.turlir.abakgists.R;

import timber.log.Timber;

public abstract class TokenizeLayout extends FrameLayout {

    public static final int INVALID_INDEX = -1;

    private int mToken;

    private Setting mLastItem;

    public TokenizeLayout(@NonNull Context context) {
        this(context, null);
    }

    public TokenizeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TokenizeLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TokenizeLayout, 0, 0);
        try {
            mToken = ta.getInteger(R.styleable.TokenizeLayout_init, 0);
        } finally {
            ta.recycle();
        }
    }

    /**
     *
     * @return максимально поддерживаемое число токенов, больше 0
     */
    public abstract int getMaxTokenCount();

    /**
     *
     * @param token токен потомка
     * @param index индекс потомка
     * @return принадлежит ли токен указанному потомку
     */
    public abstract boolean doesViewToToken(int token, int index);

    /**
     *
     * @param token токен потомка
     * @return индекс потомка, имеющего этот токен или {@link #INVALID_INDEX}
     */
    public abstract int getChildIndexByToken(int token);

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getMaxTokenCount() < 1) {
            throw new IllegalStateException("getMaxTokenCount() должен быть больше 0");
        }
        if (getChildCount() > getMaxTokenCount()) {
            throw new IllegalStateException("getChildCount() должен находится в пределах от 1 до " + getMaxTokenCount());
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

    public final int currentToken() {
        return mToken;
    }

    public final void changeToken(int value) {
        setToken(value);
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
        setToken(mToken);
    }

    /**
     * Применение токена к только что добавленному потомку
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

            if (doesViewToToken(mToken, child)) {
                mLastItem = new Setting(mToken, child);
                showChild(child);
                return true;
            } else {
                hideChild(child);
                return false;
            }

        }
    }

    /**
     * Изменение токена при наличии/отсутствии потомков
     */
    private int setToken(int group) {
        if (mLastItem != null) { // есть настройки

            if (group != mLastItem.getToken()) { // группа изменилась

                if (getChildCount() > mLastItem.getPosition() && mLastItem.getPosition() > INVALID_INDEX) {
                    hideChild(mLastItem.getPosition());
                }

                int position = getChildIndexByToken(group);
                mLastItem = new Setting(group, position);
                if (position != INVALID_INDEX) {
                    showChild(mLastItem.getPosition());
                }
            }

            return mToken = group; // иначе ничего не делаем

        } else { // нет настроек

            int index = getChildIndexByToken(group);
            if (index != INVALID_INDEX) {
                showChild(index);
                mLastItem = new Setting(group, index);
            }

            return mToken = group;
        }
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

    public static class SwitchLayoutParams extends FrameLayout.LayoutParams {

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

        private SwitchLayoutState(Parcelable in, TokenizeLayout layout) {
            super(in);
            index = layout.mToken;
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

        private void apply(TokenizeLayout layout) {
            layout.mToken = index;
        }

    }

    /**
     * Олицетворяет пару токен против позиция вью, которой он принадлежит
     * Служит для кеширования значения и удобного манипулирования значением {@link #getChildIndexByToken(int)}
     */
    private static class Setting {

        private final int mToken, mPosition;

        Setting(int token, int position) {
            mToken = token;
            mPosition = position;
        }

        private int getToken() {
            return mToken;
        }

        private int getPosition() {
            return mPosition;
        }

    }
}
