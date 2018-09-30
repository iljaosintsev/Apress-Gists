package com.turlir.abakgists.allgists.view.listing;

import androidx.recyclerview.widget.RecyclerView;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class GistListItemAnimator extends SlideInLeftAnimator {

    @Override
    protected long getAddDelay(RecyclerView.ViewHolder holder) {
        return 0;
    }

    @Override
    protected long getRemoveDelay(RecyclerView.ViewHolder holder) {
        return 0;
    }


}
