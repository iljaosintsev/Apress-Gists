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

    private DescNoteGistMember member;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gist);
        App.getComponent().inject(this);

        ButterKnife.bind(this);

        GistModel content = getIntent().getParcelableExtra(EXTRA_GIST);
        member = new DescNoteGistMember(content);

        member.applyContent();
    }

    @OnClick(R.id.btn_save)
    public void onClickSave() {
        if (member.isChange()) {
            Timber.i("Внесены изменения, обновление БД");
            GistModel now = member.createContent();
            _database.put()
                    .object(now)
                    .withPutResolver(UPDATE_RESOLVER)
                    .prepare()
                    .executeAsBlocking();
            member = new DescNoteGistMember(now);
        } else {
            Timber.i("Изменения не внесены");
        }
    }

    @OnClick(R.id.btn_web)
    public void onClickWeb() {
        Uri link = member.insteadWebLink();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(link);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (!member.isChange()) {
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

    private class DescNoteGistMember {

        private final EqualsSolver mSolver = new EqualsSolver();
        private final GistModel mContent;

        DescNoteGistMember(GistModel content) {
            mContent = content;
        }

        void applyContent() {
            loadAvatar(mContent.ownerAvatarUrl);
            login.setText(mContent.ownerLogin);
            desc.setText(mContent.description);
            note.setText(mContent.note);
        }

        GistModel createContent() {
            String descAt = desc.getText().toString();
            String noteAt = note.getText().toString();

            return new GistModel(mContent, descAt, noteAt);
        }

        boolean isChange() {
            GistModel now = createContent();
            return mSolver.solveModel(mContent, now);
        }

        Uri insteadWebLink() {
            return mContent.insteadWebLink();
        }

        private void loadAvatar(String url) {
            Picasso.with(GistActivity.this)
                    .load(url)
                    .fit()
                    .error(R.drawable.ic_github)
                    .placeholder(R.drawable.ic_github)
                    .into(avatar);
        }

    }
}
