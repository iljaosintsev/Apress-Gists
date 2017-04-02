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

    SwipeRefreshLayout mSwipe;

    private AllGistAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private final SwipeRefreshLayout.OnRefreshListener mSwipeListener
            = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (mSwipe.isEnabled()) {
                mSwipe.setEnabled(false);
                loadPage(0);
            }
        }
    };

    private final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        private static final int MAX_PAGE = 3;

        private static final int PAGE_SIZE = 30;

        private int mCurrentPage;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            boolean isLastPage = mCurrentPage >= MAX_PAGE;
            if (!mSwipe.isEnabled() && !isLastPage) {
                boolean out = (visibleItemCount + firstVisibleItemPosition + 3) >= totalItemCount;
                if (out && firstVisibleItemPosition >= 0) {
                    int page = (visibleItemCount + firstVisibleItemPosition) / PAGE_SIZE + 1;
                    loadPage(-1);
                }
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
        loadPage(0);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _presenter.attach(this);
    }

    public void onGist(List<Gist> value) {
        mAdapter.addGist(value);
        mSwipe.setRefreshing(false);
        mSwipe.setEnabled(true);
    }

    private void loadPage(int page) {
        _presenter.loadPublicGists();
    }

}
