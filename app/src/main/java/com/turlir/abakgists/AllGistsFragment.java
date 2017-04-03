package com.turlir.abakgists;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.turlir.abakgists.base.BaseFragment;
import com.turlir.abakgists.model.Gist;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class AllGistsFragment extends BaseFragment {

    @Inject
    AllGistsPresenter _presenter;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    private SwipeRefreshLayout mSwipe;
    private AllGistAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private final SwipeRefreshLayout.OnRefreshListener mSwipeListener
            = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            resetToFirstPage();
        }
    };

    private final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        private int mLastDownloadedSize = 0;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();

            boolean closeEdge = firstVisibleItem + visibleItemCount + 3 == totalItemCount;
            boolean sizeNotDownload = totalItemCount > mLastDownloadedSize;
            if (closeEdge && sizeNotDownload && !mSwipe.isRefreshing()) {
                mLastDownloadedSize = totalItemCount;
                mSwipe.setRefreshing(true);
                loadNewPage(totalItemCount);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().inject(this);
        _presenter.attach(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
        View root = inflater.inflate(R.layout.fragment_all_gists, container, false);
        butterKnifeBind(root);

        mSwipe = ((SwipeRefreshLayout) root);
        mSwipe.setOnRefreshListener(mSwipeListener);

        mAdapter = new AllGistAdapter(getContext());
        recycler.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(mLayoutManager);

        DividerItemDecoration divider =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recycler.addItemDecoration(divider);
        recycler.addItemDecoration(new SpaceDecorator(getActivity(), R.dimen.item_offset));

        recycler.addOnScrollListener(mScrollListener);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mSwipe.setRefreshing(true);
        resetToFirstPage();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _presenter.detach();
        recycler.removeOnScrollListener(mScrollListener);
    }

    @Override
    public void showError(String msg) {
        super.showError(msg);
        mSwipe.setRefreshing(false);
    }

    public void onGistLoaded(List<Gist> value) {
        mAdapter.addGist(value);
        mSwipe.setRefreshing(false);
    }

    public void stopGistLoad() {
        mSwipe.setRefreshing(false);
        recycler.removeOnScrollListener(mScrollListener);
    }

    private void resetToFirstPage() {
        mAdapter.clearGist();
        _presenter.loadPublicGists(0);
    }

    private void loadNewPage(int totalItemCount) {
        _presenter.loadPublicGists(totalItemCount);
    }

}
