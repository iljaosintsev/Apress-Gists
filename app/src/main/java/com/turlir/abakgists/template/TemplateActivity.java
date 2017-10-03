package com.turlir.abakgists.template;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.turlir.abakgists.R;
import com.turlir.abakgists.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TemplateActivity extends BaseActivity {

    @BindView(R.id.template_act_root)
    LinearLayout root;

    private SimpleForm mForm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        ButterKnife.bind(this);

        mForm = new SimpleForm(root);
        mForm.create();
        mForm.connect();
        //mForm.bind(new SimpleForm.ComplexValue("", ""));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("", mForm.collect());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        SimpleForm.ComplexValue job = savedInstanceState.getParcelable("");
        if (job != null) {
            String meta = " restored";
            mForm.bind(new SimpleForm.ComplexValue(job.first + meta, job.second + meta));
        }
    }

    @OnClick(R.id.template_act_btn_save)
    void clickSave() {
        mForm.verify();
    }

}
