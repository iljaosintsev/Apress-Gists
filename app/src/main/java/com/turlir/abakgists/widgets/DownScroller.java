package com.turlir.abakgists.widgets;

import androidx.recyclerview.widget.RecyclerView;

public class DownScroller extends Scroller {

    private static final int THRESHOLD_END = 3;

    private final NextPager mPager;

    public DownScroller(NextPager pager) {
        mPager = pager;
    }

    @Override
    void downScroll(RecyclerView recyclerView, int dx, int dy,
                  int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean closeEnd = firstVisibleItem + visibleItemCount + THRESHOLD_END >= totalItemCount;
        if (closeEnd) {
            if (!isLoaded && !mPager.isEmpty()) {
                isLoaded = true;
                recyclerView.post(mPager::loadNextPage);
            }
        }
    }

    public interface NextPager {

        void loadNextPage();

        boolean isEmpty();
    }

}
