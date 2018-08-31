package com.turlir.abakgists;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.turlir.abakgists.allgists.view.AllGistsActivity;
import com.turlir.abakgists.notes.view.NotesActivity;
import com.turlir.abakgists.widgets.anim.base.Factory;
import com.turlir.abakgists.widgets.anim.creator.Setting;
import com.turlir.abakgists.widgets.anim.factor.ButtonAnimFactory;
import com.turlir.abakgists.widgets.anim.factor.Params;

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
        if (animation != null) {
            compatReverse(animation);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        animation.removeAllListeners();
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
        Pair<Params, Params> animateParams = new Params.TwoWayBuilder(clicked)
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
        Setting common = new Setting(0, DURATION);
        Factory factory = new ButtonAnimFactory(params, common);
        return factory.animate(btnAllGists, btnNotes, btnAll);
    }

    private void compatReverse(AnimatorSet directAnim) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            oreoReverse(directAnim);
        } else {
            oldReverse(directAnim);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void oreoReverse(AnimatorSet directAnim) {
        if (directAnim.getTotalDuration() != Animator.DURATION_INFINITE) {
            directAnim.reverse();
        }
    }

    private void oldReverse(AnimatorSet directAnim) {

        class ReverseInterpolator implements Interpolator {

            private final TimeInterpolator delegate;

            private ReverseInterpolator(TimeInterpolator delegate){
                this.delegate = delegate;
            }

            private ReverseInterpolator(){
                this(new LinearInterpolator());
            }

            @Override
            public float getInterpolation(float input) {
                return 1 - delegate.getInterpolation(input);
            }
        }

        AnimatorSet back = new AnimatorSet();
        back.play(directAnim);
        back.setInterpolator(new ReverseInterpolator());
        back.start();
    }
}
