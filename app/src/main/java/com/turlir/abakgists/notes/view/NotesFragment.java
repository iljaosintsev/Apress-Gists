package com.turlir.abakgists.notes.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.turlir.abakgists.R;
import com.turlir.abakgists.allgists.view.AllGistAdapter;
import com.turlir.abakgists.base.App;
import com.turlir.abakgists.base.BaseFragment;
import com.turlir.abakgists.base.GistItemClickListener;
import com.turlir.abakgists.gist.GistActivity;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.notes.NotesPresenter;
import com.turlir.abakgists.widgets.DividerDecorator;
import com.turlir.abakgists.widgets.SwitchLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class NotesFragment extends BaseFragment implements GistItemClickListener {

    @Inject
    NotesPresenter _presenter;

    @BindView(R.id.all_gist_switch)
    SwitchLayout root;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.swipeLayout)
    View swipe;

    private AllGistAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().inject(this);
        _presenter.attach(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle saved) {
        View root = inflater.inflate(R.layout.fragment_all_gists, container, false);
        butterKnifeBind(root);

        swipe.setEnabled(false);

        mAdapter = new AllGistAdapter(getContext(), this);
        recycler.setAdapter(mAdapter);
        RecyclerView.LayoutManager lm
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(lm);

        DividerDecorator divider = new DividerDecorator(getActivity(), R.drawable.divider,
                DividerItemDecoration.VERTICAL, DividerDecorator.TOP_DIVIDER);
        recycler.addItemDecoration(divider);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _presenter.loadNotes();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _presenter.detach();
    }

    @Override
    public void onListItemClick(GistModel model, ImageView ivAvatar) {
        if (getActivity() != null) {
            Intent i = GistActivity.getStartIntent(getActivity(), model.getId());
            String tag = getString(R.string.avatar_transition_tag);
            ViewCompat.setTransitionName(ivAvatar, tag);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation
                    (getActivity(), ivAvatar, tag);
            startActivity(i, options.toBundle());
        }
    }

    public void onNotesLoaded(List<GistModel> gists) {
        root.toContent();
        mAdapter.resetGists(gists);
    }

    @Override
    public String toString() {
        return "Notes";
    }

}
