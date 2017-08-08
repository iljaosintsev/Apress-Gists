package com.turlir.abakgists;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.turlir.abakgists.allgists.view.AllGistsActivity;
import com.turlir.abakgists.notes.view.NotesActivity;
import com.turlir.abakgists.view.anim.base.Factory;
import com.turlir.abakgists.view.anim.creator.Setting;
import com.turlir.abakgists.view.anim.factor.ButtonAnimFactory;

import butterknife.BindDimen;
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

    @BindDimen(R.dimen.activity_vertical_margin)
    float margin;

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_all_gists)
    public void onClickAllGists(View v) {
        ButtonAnimFactory.Params params = new ButtonAnimFactory.Params()
                .alpha(1, 0, 0)
                .addit(margin, 0, 0)
                .multi(1, 1, 1);
        animateButton(params, AllGistsActivity.class);
    }

    @OnClick(R.id.btn_notes)
    public void onClickNotes() {
        ButtonAnimFactory.Params params = new ButtonAnimFactory.Params()
                .alpha(0, 1, 0)
                .addit(0, 0, 0)
                .multi(-1, 0, 1);
        animateButton(params, NotesActivity.class);
    }

    @OnClick(R.id.btn_all_in_one)
    public void clickAllInOne() {
        ButtonAnimFactory.Params params = new ButtonAnimFactory.Params()
                .alpha(0, 0, 1)
                .addit(0, 0, -margin)
                .multi(-1, -1, -1);
        animateButton(params, AllInOneActivity.class);
    }

    private void animateButton(final ButtonAnimFactory.Params params, final Class clazz) {
        Setting common = new Setting(0, DURATION, 0);
        Factory factory = new ButtonAnimFactory(params, common);
        AnimatorSet set = factory.animate(btnAllGists, btnNotes, btnAll);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent i = new Intent(getApplicationContext(), clazz);
                startActivity(i);
            }
        });
    }


}
