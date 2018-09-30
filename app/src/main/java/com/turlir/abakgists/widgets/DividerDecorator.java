package com.turlir.abakgists.widgets;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;


public class DividerDecorator extends RecyclerView.ItemDecoration  {

    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    public static final int DOUBLE_DIVIDER = 0;
    public static final int TOP_DIVIDER = 1;
    public static final int NO_DIVIDER = 1 << 1;

    private Drawable mDivider;

    private int mOrientation;

    private final Rect mBounds;

    private int shouldLastDivider;

    /**
     * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
     * {@link LinearLayoutManager}.
     *  @param context Current context, it will be used to access resources.
     * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     * @param shouldLastDivider strategy for flexibility last item decoration
     */
    public DividerDecorator(Context context, @DrawableRes int divider, int orientation,
                            @DECORATION int shouldLastDivider) {
        mBounds = new Rect();
        mDivider = ContextCompat.getDrawable(context, divider);
        setOrientation(orientation);
        this.shouldLastDivider = shouldLastDivider;
    }

    /**
     * Sets the orientation for this divider. This should be called if
     * {@link RecyclerView.LayoutManager} changes orientation.
     *
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException(
                    "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        mOrientation = orientation;
    }

    /**
     * Sets the {@link Drawable} for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    public void setDrawable(@NonNull Drawable drawable) {
        mDivider = drawable;
    }

    @DECORATION
    public int lastDividerStrategy() {
        return shouldLastDivider;
    }

    public void setLastDividerStategy(@DECORATION int value) {
        shouldLastDivider = value;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null || parent.getChildCount() == 1) {
            return;
        }
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    @SuppressLint("NewApi")
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(
                    left,
                    parent.getPaddingTop(),
                    right,
                    parent.getHeight() - parent.getPaddingBottom()
            );
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = maxChildIndex(parent);
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    @SuppressLint("NewApi")
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int top;
        final int bottom;
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(
                    parent.getPaddingLeft(),
                    top,
                    parent.getWidth() - parent.getPaddingRight(),
                    bottom
            );
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        final int childCount = maxChildIndex(parent);
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            final int right = mBounds.right + Math.round(child.getTranslationX());
            final int left = right - mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (parent.getChildCount() == 1) return;
        if (mOrientation == VERTICAL) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }

    private int maxChildIndex(RecyclerView parent) {
        LinearLayoutManager llm = (LinearLayoutManager) parent.getLayoutManager();
        int lv = llm.findLastVisibleItemPosition();
        int all = llm.getItemCount();
        boolean endOfList = all == lv + 1;
        if (endOfList) {
            int delta = lastItemDeltaFactory();
            return parent.getChildCount() - delta;
        } else {
            return parent.getChildCount();
        }
    }

    private int lastItemDeltaFactory() {
        switch (lastDividerStrategy()) {
            case DOUBLE_DIVIDER:
                return 0;
            case TOP_DIVIDER:
               return 1;
            case NO_DIVIDER:
                return  2;
        }
        return 2; // no one else
    }

    @IntDef(flag = true, value = {
            DOUBLE_DIVIDER, TOP_DIVIDER, NO_DIVIDER
    })
    public @interface DECORATION {

    }

}
