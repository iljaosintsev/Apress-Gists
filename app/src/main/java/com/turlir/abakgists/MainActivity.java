package com.turlir.abakgists;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.turlir.abakgists.allgists.view.AllGistsActivity;
import com.turlir.abakgists.notes.view.NotesActivity;
import com.turlir.abakgists.view.anim.base.Factory;
import com.turlir.abakgists.view.anim.creator.Setting;
import com.turlir.abakgists.view.anim.factor.ButtonAnimFactory;
import com.turlir.abakgists.view.anim.factor.Params;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int DURATION = 750;

    @BindView(R.id.btn_all_gists)
    View btnAllGists;

    @BindView(R.id.btn_notes)
    View btnNotes;

    @BindView(R.id.btn_all_in_one)
    View btnAll;

    private Pair<Params, Params> animateParams;
    private AnimatorSet animation;

    @Override
    protected void onCreate(@Nullable Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animateParams != null) {
            AnimatorSet set = animateButton(animateParams.second);
            set.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (animation != null && animation.isStarted()) {
            animation.end();
        }
    }

    @OnClick(R.id.btn_all_gists)
    public void onClickAllGists(View v) {
        buttonClick(btnAllGists, AllGistsActivity.class);
    }

    @OnClick(R.id.btn_notes)
    public void onClickNotes() {
        buttonClick(btnNotes, NotesActivity.class);
    }

    @OnClick(R.id.btn_all_in_one)
    public void clickAllInOne() {
        buttonClick(btnAll, AllInOneActivity.class);
    }

    private void buttonClick(View clicked, final Class clazz) {
        animateParams = new Params.TwoWayBuilder(clicked)
                .up(btnAllGists)
                .center(btnNotes)
                .down(btnAll)
                .compute();
        animation = animateButton(animateParams.first);
        animation.start();
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent i = new Intent(getApplicationContext(), clazz);
                startActivity(i);
            }
        });
    }

    private AnimatorSet animateButton(final Params params) {
        Setting common = new Setting(0, DURATION, 0);
        Factory factory = new ButtonAnimFactory(params, common);
        return factory.animate(btnAllGists, btnNotes, btnAll);
    }


}
