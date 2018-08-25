package com.turlir.abakgists.widgets;

import android.support.v7.widget.RecyclerView;

public class UpScroller extends Scroller {

    private static final int THRESHOLD_UP = 7;

    private final PrevPager mPager;

    public UpScroller(PrevPager pager) {
        mPager = pager;
    }

    @Override
    void upScroll(RecyclerView recyclerView, int dx, int dy,
                  int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean closeUp = firstVisibleItem - THRESHOLD_UP <= 0;
        if (closeUp) {
            if (!isLoaded && !mPager.isEmpty()) {
                isLoaded = true;
                recyclerView.post(mPager::loadPrevPage);
            }
        }
    }

    public interface PrevPager {

        void loadPrevPage();

        boolean isEmpty();
    }

}
