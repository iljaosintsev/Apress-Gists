package com.turlir.tokenizelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Абстрактный layout для единовременного отображения только одного своего потомка.
 * Какого, решает наследник с помощью механизма токенизации.
 */
public abstract class TokenizeLayout extends FrameLayout
        implements TokenSwitcher.TokenInformator, ChildDiff.ChildManipulator {

    private final TokenSwitcher mSwitcher;

    public TokenizeLayout(@NonNull Context context) {
        this(context, null);
    }

    public TokenizeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TokenizeLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mSwitcher = new TokenSwitcher(getDefaultToken(attrs), this);
    }

    /**
     * @param token токен потомка
     * @param index индекс потомка
     * @return принадлежит ли токен указанному потомку
     */
    @Override
    public abstract boolean doesViewToToken(int token, int index);

    /**
     * @param token токен потомка
     * @return индекс потомка, имеющего этот токен или {@link TokenSwitcher#INVALID_INDEX}
     */
    @Override
    public abstract int getChildIndexByToken(int token);

    /**
     * @return максимально поддерживаемое число токенов, больше 0
     */
    protected abstract int getMaxTokenCount();

    /**
     * @param attrs атрибуты макета
     * @return токен по-умолчанию видимого потомка
     */
    protected abstract int getDefaultToken(final AttributeSet attrs);

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
    public TokenizeLayoutParams generateLayoutParams(AttributeSet attrs) {
        return new TokenizeLayoutParams(getContext(), attrs);
    }

    /**
     * @return текущий токен, по-умолчанию равен {@link #getDefaultToken(AttributeSet)}
     */
    public final int currentToken() {
        return mSwitcher.currentToken();
    }

    /**
     * Изменение токена при наличии/отсутствии потомков
     * @param value новый токен
     */
    public final void changeToken(int value) {
        ChildDiff set = mSwitcher.setToken(value);
        if (set.isEmpty()) {
            Log.w(getClass().getSimpleName(), "changeToken broken - ChildDiff is empty");
        }
        set.apply(this);
    }

    @Override
    protected TokenizeLayoutParams generateDefaultLayoutParams() {
        return new TokenizeLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected TokenizeLayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return generateDefaultLayoutParams();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable instance = super.onSaveInstanceState();
        return new TokenizeLayoutState(instance, this);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        TokenizeLayoutState now = (TokenizeLayoutState) state;
        showChild(now.index);
        mSwitcher.invalidateToken();
    }

    /**
     * Применение токена к только что добавленному потомку
     */
    private void applyGroupByChild(int child) {
        boolean isVisible = mSwitcher.applyGroupByChild(child);
        if (isVisible) {
            showChild(child);
        } else {
            hideChild(child);
        }
    }

    ///
    /// ChildManipulator
    ///

    @Override
    public void hideChild(int i) {
        View child = getChildAt(i);
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        int def = ((TokenizeLayoutParams) lp).hided;
        //noinspection WrongConstant
        child.setVisibility(def);

        getChildAt(i).setEnabled(false);
    }

    @Override
    public void showChild(int i) {
        getChildAt(i).setVisibility(View.VISIBLE);
        getChildAt(i).setEnabled(true);
    }

    public static class TokenizeLayoutParams extends FrameLayout.LayoutParams {

        final int hided;

        TokenizeLayoutParams(int width, int height) {
            super(width, height);
            hided = View.INVISIBLE;
        }

        TokenizeLayoutParams(@NonNull Context c, @Nullable AttributeSet attrs) {
            super(c, attrs);

            TypedArray ta = c.obtainStyledAttributes(attrs, R.styleable.TokenizeLayout_Layout, 0, 0);
            try {
                hided = ta.getInteger(R.styleable.TokenizeLayout_Layout_layout_hided, View.INVISIBLE); // default
            } finally {
                ta.recycle();
            }
        }

    }

    private static final class TokenizeLayoutState extends BaseSavedState {

        public static final Parcelable.Creator<TokenizeLayoutState> CREATOR
                = new Parcelable.Creator<TokenizeLayoutState>() {

            @Override
            public TokenizeLayoutState createFromParcel(Parcel in) {
                return new TokenizeLayoutState(in);
            }

            @Override
            public TokenizeLayoutState[] newArray(int size) {
                return new TokenizeLayoutState[size];
            }
        };

        final int index;

        private TokenizeLayoutState(Parcelable in, TokenizeLayout layout) {
            super(in);
            index = layout.mSwitcher.currentToken();
        }

        private TokenizeLayoutState(Parcel source) {
            super(source);
            index = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(index);
        }
    }
}
