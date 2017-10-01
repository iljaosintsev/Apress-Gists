package com.turlir.abakgists;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.LinearLayout;

import com.turlir.abakgists.base.BaseActivity;
import com.turlir.abakgists.templater.Form;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TemplateActivity extends BaseActivity {

    @BindView(R.id.template_act_root)
    LinearLayout root;

    private Form mForm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity);
        ButterKnife.bind(this);

        mForm = new Form(this, new DynamicChecker());
        //mForm.create();
        mForm.connect(root);
        mForm.bind();
    }

    @OnClick(R.id.template_act_btn_save)
    void clickSave() {
        mForm.verify();
    }

    private class DynamicChecker implements Form.CompleteCallback {

        @Override
        public void onComplete(Form.ComplexValue value) {
            if (value.second.equals("i need you")) {
                mForm.showSecondFieldError("citizen erased");
            } else {
                new AlertDialog.Builder(TemplateActivity.this)
                        .setTitle("Result")
                        .setMessage("Success")
                        .setPositiveButton(android.R.string.ok, null)
                        .create()
                        .show();
            }
        }

    }
}
