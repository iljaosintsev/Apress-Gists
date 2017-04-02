package com.turlir.abakgists;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class SpaceDecorator extends RecyclerView.ItemDecoration {

    private int mOffset;

    SpaceDecorator(Context cnt, @DimenRes int offset) {
        mOffset = (int) cnt.getResources().getDimension(offset);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int index = parent.getChildLayoutPosition(view);
        if (index != RecyclerView.NO_POSITION && index > 0) {
            outRect.top += mOffset;
        }
    }

}
