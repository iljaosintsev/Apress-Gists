package com.turlir.abakgists.gist;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
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

    @BindView(R.id.tv_login)
    TextView tvLogin;

    @BindView(R.id.iv_avatar)
    ImageView avatar;

    @BindView(R.id.et_desc)
    EditText desc;

    @BindView(R.id.et_note)
    EditText note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gist);
        App.getComponent().inject(this);

        ButterKnife.bind(this);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gist_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gist_menu_delete:
                new AlertDialog.Builder(this)
                        .setTitle("Last chance")
                        .setMessage("Do you want to delete this gist?")
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            _presenter.delete();
                        })
                        .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                            dialog.cancel();
                        })
                        .create()
                        .show();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_GIST, _presenter.content);
    }

    @OnClick(R.id.btn_save)
    public void onClickSave() {
        if (_presenter.isChange(desc.getText().toString(), note.getText().toString())) {
            Timber.i("Внесены изменения, обновление БД");
            _presenter.transact(desc.getText().toString(), note.getText().toString());
        } else {
            Timber.i("Изменения не внесены");
        }
    }

    @OnClick(R.id.btn_web)
    public void onClickWeb() {
        Uri link = _presenter.content.insteadWebLink();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(link);
        startActivity(i);
    }

    public void deleteSuccess() {
        Snackbar.make(findViewById(android.R.id.content), "Gist successfully deleted", Snackbar.LENGTH_LONG).show();
        onBackPressed();
    }

    public void deleteFailure() {
        Snackbar.make(findViewById(android.R.id.content), "Error when deleting gist", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (!_presenter.isChange(desc.getText().toString(), note.getText().toString())) {
            GistActivity.super.onBackPressed();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.there_changes)
                    .setMessage(R.string.save_quest)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        onClickSave();
                        dialog.dismiss();
                        onBackPressed();
                    })
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                        dialog.cancel();
                        onBackPressed();
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

        final String login;
        if (content.ownerLogin != null) {
            login = content.ownerLogin;
        } else {
            login = getString(R.string.anonymous);
        }
        tvLogin.setText(login);

        desc.setText(content.description);
        note.setText(content.note);
    }
}
