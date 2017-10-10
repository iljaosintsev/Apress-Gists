package com.turlir.abakgists.template;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.LinearLayout;

import com.turlir.abakgists.R;
import com.turlir.abakgists.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TemplateActivity extends BaseActivity {

    @BindView(R.id.template_act_root)
    LinearLayout root;

    private LoginForm mForm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        ButterKnife.bind(this);

        mForm = new LoginForm(root);
        mForm.create();
        mForm.connect();
        EditableProfile profile = new EditableProfile(
                "Илья",
                "vk.com/turlir",
                "Программист",
                "+7 953 820 77-55",
                "@turlir",
                "iljaosincev@gmail.com",
                true,
                ""
        );
        mForm.bind(profile);
    }

    @OnClick(R.id.template_act_btn_save)
    void clickSave() {
        boolean verify = mForm.verify();
        if (verify) {
            EditableProfile nowProfile = mForm.collect();
            new AlertDialog.Builder(this)
                    .setTitle("Profile")
                    .setMessage(nowProfile.toString())
                    .setNeutralButton(android.R.string.ok, null)
                    .create()
                    .show();
        }
    }

    @OnClick(R.id.template_act_btn_phone_error)
    void clickErrorPhone() {
        mForm.showError("phone", "Example target error");
    }

}
