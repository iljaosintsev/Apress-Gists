package com.turlir.abakgists.allgists.view;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.turlir.abakgists.R;
import com.turlir.abakgists.allgists.AllGistsPresenter;
import com.turlir.abakgists.base.BaseFragment;
import com.turlir.abakgists.base.GistItemClickListener;
import com.turlir.abakgists.gist.GistActivity;
import com.turlir.abakgists.model.ErrorModel;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.LoadingModel;
import com.turlir.abakgists.widgets.DividerDecorator;
import com.turlir.abakgists.widgets.DownScroller;
import com.turlir.abakgists.widgets.SwitchLayout;
import com.turlir.abakgists.widgets.UpScroller;

import java.util.List;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import timber.log.Timber;


public class AllGistsFragment extends BaseFragment implements GistListView, GistItemClickListener,
        DownScroller.NextPager, UpScroller.PrevPager {

    private static final int MIN_COUNT = 2;

    @InjectPresenter
    AllGistsPresenter _presenter;

    @BindView(R.id.all_gist_switch)
    SwitchLayout root;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipe;

    private AllGistAdapter mAdapter;
    private DownScroller mForwardScrollListener;
    private UpScroller mBackwardScrollListener;

    private final SwipeRefreshLayout.OnRefreshListener mSwipeListener = () -> {
        _presenter.updateGist();
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle saved) {
        View root = inflater.inflate(R.layout.fragment_all_gists, container, false);
        butterKnifeBind(root);

        swipe.setOnRefreshListener(mSwipeListener);

        mForwardScrollListener = new DownScroller(this);
        recycler.addOnScrollListener(mForwardScrollListener);
        mBackwardScrollListener = new UpScroller(this);
        recycler.addOnScrollListener(mBackwardScrollListener);

        recycler.setItemAnimator(new SlideInLeftAnimator());

        // start
        TextView tv = root.findViewById(R.id.in_loading_tv);
        Drawable[] pd = tv.getCompoundDrawables();
        for (Drawable drawable : pd) {
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            _presenter.firstLoad();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Context cnt = getActivity();

        mAdapter = new AllGistAdapter(cnt, this);
        recycler.setAdapter(mAdapter);
        LinearLayoutManager lm = new LinearLayoutManager(cnt, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(lm);

        DividerDecorator divider = new DividerDecorator(
                cnt,
                R.drawable.divider,
                DividerDecorator.VERTICAL,
                DividerDecorator.TOP_DIVIDER
        );
        recycler.addItemDecoration(divider);
    }

    @Override
    public void onDestroyView() {
        recycler.clearOnScrollListeners();
        swipe.setOnRefreshListener(null);
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _presenter.detachView(this);
    }

    ///
    /// View
    ///

    @Override
    public void onListItemClick(GistModel model, ImageView ivAvatar) {
        if (getActivity() != null) {
            Intent i = GistActivity.getStartIntent(getActivity(), model);
            String tag = getString(R.string.avatar_transition_tag);
            ViewCompat.setTransitionName(ivAvatar, tag);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation
                    (getActivity(), ivAvatar, tag);
            startActivity(i, options.toBundle());
        }
    }

    ///
    /// Presenter
    ///

    @Override
    public void onGistLoaded(List<GistModel> value, boolean resetForward, boolean resetBackward) {
        Timber.d("%d elements put in ui, %s resetForward, %s resetBackward", value.size(), resetForward, resetBackward);
        if (!isEmpty()) {
            mAdapter.removeLastIfLoading();
            swipe.setRefreshing(false);
        }
        mAdapter.resetGists(value);
        if (resetForward) {
            mForwardScrollListener.reset();
        }
        if (resetBackward) {
            mBackwardScrollListener.reset();
        }
    }

    ////
    //// Pager(s)
    ////

    @Override
    public void loadNextPage() {
        Timber.i("scrolling initial request next page");
        _presenter.nextPage();
    }

    @Override
    public void loadPrevPage() {
        Timber.i("scrolling initial request previous page");
        _presenter.prevPage();
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.getItemCount() < MIN_COUNT;
    }

    ///
    /// Error interpreter
    ///

    @Override
    public void nonBlockingError(String msg) {
        swipe.setRefreshing(false);
        Snackbar.make(recycler, msg, Snackbar.LENGTH_LONG).show();
        mForwardScrollListener.reset();
        mBackwardScrollListener.reset();
    }

    @Override
    public void alertError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void blockingError(ErrorModel model) {
        swipe.setRefreshing(false);
        mAdapter.clearAll(); // что бы ошибки не накапливались с обновлением
        mAdapter.addError(model);
        if (recycler.getAdapter() == null) {
            recycler.setAdapter(mAdapter);
        }
    }

    @Override
    public void toBlockingLoad(boolean visible) {
        if (visible) {
            root.toLoading();
        } else {
            root.toContent();
        }
    }

    @Override
    public void inlineLoad(LoadingModel visible) {
        mAdapter.addLoading(visible);
    }

    @Override
    public void removeInlineLoad() {
        mAdapter.removeLastIfLoading();
    }

    @Override
    public String toString() {
        return "All Gists";
    }
}
