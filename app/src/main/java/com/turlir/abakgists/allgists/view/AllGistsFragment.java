package com.turlir.abakgists.allgists.view;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.turlir.abakgists.R;
import com.turlir.abakgists.allgists.AllGistsPresenter;
import com.turlir.abakgists.base.App;
import com.turlir.abakgists.base.BaseFragment;
import com.turlir.abakgists.base.OnClickListener;
import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.gist.GistActivity;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.widgets.DividerDecorator;
import com.turlir.abakgists.widgets.DownScroller;
import com.turlir.abakgists.widgets.SwitchLayout;
import com.turlir.abakgists.widgets.UpScroller;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import timber.log.Timber;


public class AllGistsFragment
        extends BaseFragment
        implements OnClickListener, DownScroller.NextPager, UpScroller.PrevPager, ErrorInterpreter {

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
    private DownScroller mForwardScrollListener;
    private UpScroller mBackwardScrollListener;

    private final SwipeRefreshLayout.OnRefreshListener mSwipeListener = () -> {
        _presenter.updateGist();
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().inject(this);
        _presenter.attach(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle saved) {
        View root = inflater.inflate(R.layout.fragment_all_gists, container, false);
        butterKnifeBind(root);

        swipe.setOnRefreshListener(mSwipeListener);

        Context cnt = getActivity();

        mAdapter = new AllGistAdapter(getContext(), this);
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
        if (savedInstanceState != null) {
           // _presenter.again(); // TODO
        } else {
            _presenter.firstLoad();
        }
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
        _presenter.detach();
    }

    ///
    /// View
    ///

    @Override
    public void onListItemClick(int position) {
        GistModel g = mAdapter.getGistByPosition(position);
        if (g != null) {
            Intent i = GistActivity.getStartIntent(getActivity(), g);
            startActivity(i);
        }
    }

    ///
    /// Presenter
    ///

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

    private void resetScroller() {
        mForwardScrollListener.reset();
        mBackwardScrollListener.reset();
    }

    public void onUpdateSuccessful() {
        mAdapter.clearAll();
        swipe.setRefreshing(false);
    }

    public boolean isError() {
        return mAdapter.getItemCount() == 1 && mAdapter.getGistByPosition(0) == null;
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
        resetScroller();
    }

    @Override
    public void alertError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void blockingError(String msg) {
        swipe.setRefreshing(false);
        mAdapter.clearAll(); // что бы ошибки не накапливались с обновлением
        mAdapter.addError(msg);
    }

    @Override
    public String toString() {
        return "All Gists";
    }

    public void toBlockingLoad(boolean visible) {
        if (visible) {
            root.toLoading();
        } else {
            root.toContent();
        }
    }

    public void inlineLoad(boolean visible) {
        if (visible) {
            mAdapter.addLoading(_presenter.trueSize());
        } else {
            mAdapter.removeLastIfLoading();
        }
    }
}
