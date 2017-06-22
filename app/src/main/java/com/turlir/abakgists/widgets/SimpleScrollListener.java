package com.turlir.abakgists.widgets;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class SimpleScrollListener extends RecyclerView.OnScrollListener {

    private static final int THRESHOLD = 3;

    private final Paginator mPg;
    private int mLastDownloadedSize;

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

        boolean closeEdge = firstVisibleItem + visibleItemCount + THRESHOLD >= totalItemCount;
        boolean sizeNotDownload = totalItemCount > mLastDownloadedSize;
        boolean notEmpty = !mPg.isEmpty();
        boolean notRefreshing = !mPg.isRefreshing();

        if (closeEdge && sizeNotDownload && notRefreshing && notEmpty) {
            mLastDownloadedSize = totalItemCount;
            // load next page
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mPg.loadNextPage();
                }
            });
        }
    }

    public interface Paginator {

        boolean isRefreshing();

        void loadNextPage();

        boolean isEmpty();
    }

}
