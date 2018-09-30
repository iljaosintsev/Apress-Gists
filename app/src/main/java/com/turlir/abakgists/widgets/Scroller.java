package com.turlir.abakgists.widgets;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class Scroller extends RecyclerView.OnScrollListener {

    boolean isLoaded;

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (lm == null) {
            return;
        }
        int firstVisibleItem = lm.findFirstVisibleItemPosition();
        if (firstVisibleItem == RecyclerView.NO_POSITION) {
            return; // игнор, если список пустой
        }
        int visibleItemCount = lm.getChildCount();
        int totalItemCount = lm.getItemCount();
        onScroll(recyclerView, dx, dy, firstVisibleItem, visibleItemCount, totalItemCount);
    }

    void upScroll(RecyclerView recyclerView, int dx, int dy,
                    int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //
    }

    void downScroll(RecyclerView recyclerView, int dx, int dy,
                    int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //
    }

    void onScroll(RecyclerView recyclerView, int dx, int dy,
                          int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (dy > 0) {
            downScroll(recyclerView, dx, dy, firstVisibleItem, visibleItemCount, totalItemCount);
        } else if (dy < 0) {
            upScroll(recyclerView, dx, dy, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public void reset() {
        isLoaded = false;
    }
}
