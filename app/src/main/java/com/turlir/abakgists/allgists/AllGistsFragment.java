package com.turlir.abakgists.allgists;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.turlir.abakgists.base.App;
import com.turlir.abakgists.gist.GistActivity;
import com.turlir.abakgists.base.OnClickListener;
import com.turlir.abakgists.R;
import com.turlir.abakgists.base.SpaceDecorator;
import com.turlir.abakgists.base.BaseFragment;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.base.ItemDecoration;
import com.turlir.abakgists.utils.SwitchLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class AllGistsFragment extends BaseFragment implements OnClickListener {

    @Inject
    AllGistsPresenter _presenter;

    @BindView(R.id.all_gist_switch)
    SwitchLayout root;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipe;

    private AllGistAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private final SwipeRefreshLayout.OnRefreshListener mSwipeListener
            = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mAdapter.clearAll();
            root.toLoading();
            _presenter.resetGist();
        }
    };

    private final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        private static final int
                THRESHOLD = 3,
                MIN_COUNT = 2;

        private int mLastDownloadedSize;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();

            boolean closeEdge = firstVisibleItem + visibleItemCount + THRESHOLD >= totalItemCount;
            boolean sizeNotDownload = totalItemCount > mLastDownloadedSize;
            boolean isEmpty = totalItemCount < MIN_COUNT;
            if (closeEdge && sizeNotDownload && !swipe.isRefreshing() && !isEmpty) {
                mLastDownloadedSize = totalItemCount;
                swipe.setRefreshing(true);
                loadNewPage();
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

        swipe.setOnRefreshListener(mSwipeListener);

        mAdapter = new AllGistAdapter(getContext(), this);
        recycler.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(mLayoutManager);

        DividerItemDecoration divider = new ItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recycler.addItemDecoration(divider);
        recycler.addItemDecoration(new SpaceDecorator(getActivity(), R.dimen.item_offset));

        recycler.addOnScrollListener(mScrollListener);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        root.toLoading();
        loadNewPage();
    }

    @Override
    public void onDestroyView() {
        recycler.removeOnScrollListener(mScrollListener);
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _presenter.detach();
    }

    @Override
    public void showError(String msg) {
        swipe.setRefreshing(false);
        root.toContent();
        mAdapter.clearAll();
        mAdapter.addError();
    }

    @Override
    public void onListItemClick(int position) {
        GistModel g = mAdapter.getGistByPosition(position);
        if (g != null) {
            Intent i = GistActivity.getStartIntent(getActivity(), g);
            startActivity(i);
        }
    }

    @Override
    public String toString() {
        return getString(R.string.all_gists);
    }

    public void onGistLoaded(List<GistModel> value, int start, int count) {
        root.toContent();
        mAdapter.addGist(value, start, count);
        swipe.setRefreshing(false);
    }

    private void loadNewPage() {
        _presenter.loadPublicGists(mAdapter.getItemCount());
    }

}
