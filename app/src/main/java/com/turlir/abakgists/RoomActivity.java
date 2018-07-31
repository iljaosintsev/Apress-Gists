package com.turlir.abakgists;

import android.os.Bundle;
import android.view.View;

import com.turlir.abakgists.base.App;
import com.turlir.abakgists.base.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoomActivity extends BaseActivity {

    @Inject
    RoomPresenter _presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        ButterKnife.bind(this);
        App.getComponent().inject(this);
        _presenter.attach(this);
        _presenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        _presenter.detach();
    }

    @OnClick({R.id.btn_save})
    public void btnSave(View view) {
        _presenter.save();
    }
}
