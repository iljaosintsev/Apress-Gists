package com.turlir.abakgists;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.squareup.picasso.Picasso;
import com.turlir.abakgists.model.Gist;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GistActivity extends AppCompatActivity {

    private static final String EXTRA_GIST = "EXTRA_GIST";

    public static Intent getStartIntent(Context cnt, Gist data) {
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

    private Gist mContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gist);
        App.getComponent().inject(this);
        ButterKnife.bind(this);
        mContent = getIntent().getParcelableExtra(EXTRA_GIST);

        if (savedInstanceState == null) {
            fillControl();
        }
    }

    @OnClick(R.id.btn_save)
    public void onClickSave() {
        String oldDesc = mContent.description;
        String oldNote = mContent.note;

        String newDesc = desc.getText().toString();
        String newNote = note.getText().toString();

        boolean descChanged = !newDesc.equals(oldDesc);
        boolean noteChanged = !newNote.equals(oldNote);

        if (descChanged || noteChanged) {
            mContent.description = newDesc;
            mContent.note = newNote;
            _database.put()
                    .object(mContent)
                    .prepare()
                    .executeAsBlocking();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onClickSave();
    }

    private void fillControl() {
        if (mContent.ownerLogin != null) {
            login.setText(mContent.ownerLogin);
        }
        if (mContent.ownerAvatarUrl != null) {
            loadAvatar(mContent.ownerAvatarUrl);
        }
        desc.setText(mContent.description);
        note.setText(mContent.note);
    }

    private void loadAvatar(String url) {
        Picasso.with(this)
                .load(url)
                .fit()
                .into(avatar);
    }

}
