package com.turlir.abakgists;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle save) {
        super.onCreate(save);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_all_gists)
    public void onClickAllGists() {
        Intent i = new Intent(this, AllGistsActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_notes)
    public void onClickNotes() {
        Intent i = new Intent(this, NotesActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_all_in_one)
    public void clickAllInOne() {
        Intent i = new Intent(this, AllInOneActivity.class);
        startActivity(i);
    }

}
