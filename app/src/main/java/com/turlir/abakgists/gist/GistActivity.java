package com.turlir.abakgists.gist;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.squareup.picasso.Picasso;
import com.turlir.abakgists.R;
import com.turlir.abakgists.base.App;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.GistModelStorIOSQLitePutResolver;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class GistActivity extends AppCompatActivity {

    private static final String EXTRA_GIST = "EXTRA_GIST";

    private static final EqualsSolver SOLVER = new EqualsSolver();

    private static final GistModelStorIOSQLitePutResolver UPDATE_RESOLVER
            = new GistModelStorIOSQLitePutResolver();

    public static Intent getStartIntent(Context cnt, GistModel data) {
        Intent i = new Intent(cnt, GistActivity.class);
        i.putExtra(EXTRA_GIST, data);
        return i;
    }

    @Inject
    StorIOSQLite _database;

    @BindView(R.id.tv_login)
    TextView login;

    @BindView(R.id.iv_avatar)
    ImageView avatar;

    @BindView(R.id.et_edsc)
    EditText desc;

    @BindView(R.id.et_note)
    EditText note;

    private GistModel mContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gist);
        App.getComponent().inject(this);
        ButterKnife.bind(this);
        mContent = getIntent().getParcelableExtra(EXTRA_GIST);

        applyContent();
    }

    @OnClick(R.id.btn_save)
    public void onClickSave() {
        String newDesc = desc.getText().toString();
        String newNote = note.getText().toString();
        if (isChange(newDesc, newNote)) {
            Timber.i("Внесены изменения, обновление БД");
            GistModel now = createContent(newDesc, newNote);
            _database.put()
                    .object(now)
                    .withPutResolver(UPDATE_RESOLVER)
                    .prepare()
                    .executeAsBlocking();
            mContent = now;
        } else {
            Timber.i("Изменения не внесены");
        }
    }

    @OnClick(R.id.btn_web)
    public void onClickWeb() {
        Uri link = mContent.insteadWebLink();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(link);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (!isChange(desc.getText().toString(), note.getText().toString())) {
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

    private boolean isChange(String newDesc, String newNote) {
        String oldDesc = mContent.description;
        String oldNote = mContent.note;
        return SOLVER.solveDescAndNote(oldDesc, newDesc, oldNote, newNote);
    }

    private void applyContent() {
        loadAvatar(mContent.ownerAvatarUrl);
        login.setText(mContent.ownerLogin);
        desc.setText(mContent.description);
        note.setText(mContent.note);
    }

    private GistModel createContent(final String newDesc, final String newNote) {
        return new GistModel(mContent, new GistModel.SideEffect() {
            @Override
            public void apply(GistModel origin) {
                origin.description = newDesc;
                origin.note = newNote;
            }
        });
    }

    private void loadAvatar(String url) {
        Picasso.with(this)
                .load(url)
                .fit()
                .error(R.drawable.ic_github)
                .placeholder(R.drawable.ic_github)
                .into(avatar);
    }

}
