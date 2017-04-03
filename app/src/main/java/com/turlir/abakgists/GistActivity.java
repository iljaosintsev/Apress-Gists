package com.turlir.abakgists;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class GistActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context cnt) {
        Intent i = new Intent(cnt, GistActivity.class);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gist);
    }

}
