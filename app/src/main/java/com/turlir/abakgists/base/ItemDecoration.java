package com.turlir.abakgists.base;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemDecoration extends DividerItemDecoration {


    public ItemDecoration(Context context, int orientation) {
        super(context, orientation);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int ic = parent.getAdapter().getItemCount();
        if (ic > 1) {
            super.onDraw(c, parent, state);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int ic = parent.getAdapter().getItemCount();
        if (ic > 1) {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }
}
