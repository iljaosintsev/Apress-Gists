package com.turlir.abakgists;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.turlir.abakgists.model.Gist;

public class GistActivity extends AppCompatActivity {

    private static final String EXTRA_GIST = "EXTRA_GIST";

    public static Intent getStartIntent(Context cnt, Gist data) {
        Intent i = new Intent(cnt, GistActivity.class);
        i.putExtra(EXTRA_GIST, data);
        return i;
    }

    private Gist mContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gist);
        mContent = getIntent().getParcelableExtra(EXTRA_GIST);
    }

}
