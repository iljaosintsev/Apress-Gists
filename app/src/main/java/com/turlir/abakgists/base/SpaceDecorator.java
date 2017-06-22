package com.turlir.abakgists.base;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceDecorator extends RecyclerView.ItemDecoration {

    private int mLeft;
    private int mTop;
    private int mRight;
    private int mBottom;

    public SpaceDecorator(Context cnt, int all) {
        this(cnt, all, all, all, all);
    }

    public SpaceDecorator(Context cnt, @DimenRes int left, @DimenRes int top, @DimenRes int right, @DimenRes int bottom) {
        mLeft = (int) cnt.getResources().getDimension(left);
        mTop = (int) cnt.getResources().getDimension(top);
        mRight = (int) cnt.getResources().getDimension(right);
        mBottom = (int) cnt.getResources().getDimension(bottom);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int index = parent.getChildLayoutPosition(view);
        if (index != RecyclerView.NO_POSITION) {
            outRect.left += mLeft;
            outRect.top += mTop;
            outRect.right += mRight;
            outRect.bottom += mBottom;
        }
    }

}
