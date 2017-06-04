package com.turlir.abakgists;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.turlir.abakgists.utils.SwitchLayout;
import com.turlir.abakgists.utils.Switching;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SwitchActivity extends AppCompatActivity {

    @BindView(R.id.switchLayout)
    Switching layout;

    @BindView(R.id.radio_content)
    CompoundButton content;

    @BindView(R.id.radio_error)
    CompoundButton error;

    @BindView(R.id.radio_loading)
    CompoundButton loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
        ButterKnife.bind(this);

        int c = layout.currentGroup();
        switch (c) {
            case SwitchLayout.CONTENT:
                content.setChecked(true);
                break;
            case SwitchLayout.ERROR:
                error.setChecked(true);
                break;
            case SwitchLayout.LOADING:
                loading.setChecked(true);
                break;
        }
    }

    @OnClick({R.id.radio_content, R.id.radio_error, R.id.radio_loading})
    public void checked(View v) {
        switch (v.getId()) {
            case R.id.radio_content:
                layout.toContent();
                break;
            case R.id.radio_error:
                layout.toError();
                break;
            case R.id.radio_loading:
                layout.toLoading();
                break;
        }
    }

}
