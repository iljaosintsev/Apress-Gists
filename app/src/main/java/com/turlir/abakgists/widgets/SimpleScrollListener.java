package com.turlir.abakgists.widgets;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class SimpleScrollListener extends RecyclerView.OnScrollListener {

    private static final int THRESHOLD_END = 3, THRESHOLD_UP = 7;

    private final Paginator mPg;
    private int mNextDownloadSize, mPrevDownloadSize;

    public SimpleScrollListener(Paginator pg) {
        mPg = pg;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisibleItem = lm.findFirstVisibleItemPosition();
        if (firstVisibleItem == RecyclerView.NO_POSITION) return; // игнор, если список пустой
        int visibleItemCount = lm.getChildCount();
        int totalItemCount = lm.getItemCount();

        if (dy > 0) {
            boolean closeEnd = firstVisibleItem + visibleItemCount + THRESHOLD_END >= totalItemCount;
            if (closeEnd) {
                boolean sizeNotDownload = totalItemCount > mNextDownloadSize;
                if (sizeNotDownload && !mPg.isEmpty()) {
                    mNextDownloadSize = totalItemCount;
                    recyclerView.post(mPg::loadNextPage);
                }
            }
        } else if (dy < 0) {
            boolean closeUp = firstVisibleItem - THRESHOLD_UP <= 0;
            if (closeUp) {
                boolean sizeNotDownload = totalItemCount > mPrevDownloadSize;
                if (sizeNotDownload && !mPg.isEmpty()) {
                    mPrevDownloadSize = totalItemCount;
                    recyclerView.post(mPg::loadPrevPage);
                }
            }
        }
    }

    public interface Paginator {

        void loadNextPage();

        void loadPrevPage();

        boolean isEmpty();
    }

}
