package com.turlir.abakgists.gist;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.turlir.abakgists.R;
import com.turlir.abakgists.base.App;
import com.turlir.abakgists.base.BaseActivity;
import com.turlir.abakgists.model.GistModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class GistActivity extends BaseActivity {

    private static final String EXTRA_GIST = "EXTRA_GIST";
    public static Intent getStartIntent(Context cnt, GistModel data) {
        Intent i = new Intent(cnt, GistActivity.class);
        i.putExtra(EXTRA_GIST, data);
        return i;
    }

    @Inject
    GistPresenter _presenter;

    @BindView(R.id.gist_act_root)
    View root;

    @BindView(R.id.tv_login)
    TextView tvLogin;

    @BindView(R.id.iv_avatar)
    ImageView avatar;

    @BindView(R.id.et_desc)
    EditText desc;

    @BindView(R.id.et_note)
    EditText note;

    @BindView(R.id.btn_save)
    View btnSave;

    private ViewTreeObserver.OnGlobalLayoutListener mKeyboardListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int heightDiff = root.getRootView().getHeight() - root.getHeight();
            if (heightDiff > dpToPx(getContext(), 200)) {
                btnSave.setVisibility(View.GONE);
            } else {
                btnSave.setVisibility(View.VISIBLE);
            }
        }

        private float dpToPx(Context context, float valueInDp) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gist);
        App.getComponent().inject(this);

        ButterKnife.bind(this);

        root.getViewTreeObserver().addOnGlobalLayoutListener(mKeyboardListener);

        final GistModel content;
        if (savedInstanceState == null) {
            content = getIntent().getParcelableExtra(EXTRA_GIST);
        } else {
            content = savedInstanceState.getParcelable(EXTRA_GIST);
        }
        _presenter.attach(this, content);

        applyContent(content);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_GIST, _presenter.content);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _presenter.detach();
        root.getViewTreeObserver().removeOnGlobalLayoutListener(mKeyboardListener);
    }

    @OnClick(R.id.btn_save)
    public void onClickSave() {
        if (_presenter.isChange(desc.getText().toString(), note.getText().toString())) {
            Timber.i("Внесены изменения, обновление БД");
            _presenter.transact(desc.getText().toString(), note.getText().toString());
        } else {
            Timber.i("Изменения не внесены");
        }
        finish();
    }

    @OnClick(R.id.btn_web)
    public void onClickWeb() {
        Uri link = _presenter.content.insteadWebLink();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(link);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (!_presenter.isChange(desc.getText().toString(), note.getText().toString())) {
            GistActivity.super.onBackPressed();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.there_changes)
                    .setMessage(R.string.save_quest)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onClickSave();
                            dialog.dismiss();
                            GistActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            GistActivity.super.onBackPressed();
                        }
                    })
                    .create()
                    .show();
        }
    }

    private void applyContent(GistModel content) {
        Picasso.with(GistActivity.this)
                .load(content.ownerAvatarUrl)
                .fit()
                .error(R.drawable.ic_github)
                .placeholder(R.drawable.ic_github)
                .into(avatar);

        tvLogin.setText(content.login(getContext()));

        desc.setText(content.description);
        note.setText(content.note);
    }
}
