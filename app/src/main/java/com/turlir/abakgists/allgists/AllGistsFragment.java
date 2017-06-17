package com.turlir.abakgists.allgists;


import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.turlir.abakgists.base.App;
import com.turlir.abakgists.gist.GistActivity;
import com.turlir.abakgists.base.OnClickListener;
import com.turlir.abakgists.R;
import com.turlir.abakgists.base.SpaceDecorator;
import com.turlir.abakgists.base.BaseFragment;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.base.ItemDecoration;
import com.turlir.abakgists.widgets.SimpleScrollListener;
import com.turlir.abakgists.widgets.SwitchLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class AllGistsFragment extends BaseFragment implements OnClickListener, SimpleScrollListener.Paginator {

    private static final int MIN_COUNT = 2;

    @Inject
    AllGistsPresenter _presenter;

    @BindView(R.id.all_gist_switch)
    SwitchLayout root;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipe;

    private AllGistAdapter mAdapter;

    private final SwipeRefreshLayout.OnRefreshListener mSwipeListener
            = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipe.setRefreshing(false);

            mAdapter.clearAll();
            root.toLoading();
            _presenter.resetGist();

            recycler.removeOnScrollListener(mScrollListener);
            mScrollListener = new SimpleScrollListener(AllGistsFragment.this);
            recycler.addOnScrollListener(mScrollListener);
        }
    };

    private RecyclerView.OnScrollListener mScrollListener;

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
        LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(lm);

        ItemDecoration divider = new ItemDecoration(getActivity(), ItemDecoration.VERTICAL);
        recycler.addItemDecoration(divider);
        recycler.addItemDecoration(new SpaceDecorator(getActivity(), R.dimen.item_offset));

        mScrollListener = new SimpleScrollListener(this);
        recycler.addOnScrollListener(mScrollListener);

        // start
        this.root.toLoading();
        TextView tv = (TextView) root.findViewById(R.id.in_loading_tv);
        Drawable[] pd = tv.getCompoundDrawables();
        for (Drawable drawable : pd) {
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
        }

        loadNextPage();

        return root;
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

    public void onGistLoaded(List<GistModel> value) {
        root.toContent();
        if (!isEmpty()) {
            mAdapter.removeLastIfLoading();
        }
        mAdapter.addGist(value);
        setRefreshing(false);
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
    public void showError(String msg) {
        swipe.setRefreshing(false);
        if (!isEmpty()) {
            super.showError(msg);
        } else {
            root.toContent();
            mAdapter.clearAll();
            mAdapter.addError();
        }
    }

    ////
    //// Paginator
    ////

    @Override
    public boolean isRefreshing() {
        return swipe.isRefreshing();
    }

    @Override
    public void setRefreshing(boolean state) {
        swipe.setRefreshing(state);

    }

    @Override
    public void loadNextPage() {
        if (!isEmpty()) {
            mAdapter.addLoading();
        }
        _presenter.loadPublicGists(mAdapter.getItemCount());
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.getItemCount() < MIN_COUNT;
    }

    @Override
    public String toString() {
        return "All Gists";
    }

}
