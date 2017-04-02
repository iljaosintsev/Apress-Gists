package com.turlir.abakgists.base;


import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseFragment extends Fragment implements BaseView {

    private List<Unbinder> mUnbinders = new ArrayList<>();

    protected Unbinder butterKnifeBind(View view) {
        Unbinder unbinder = ButterKnife.bind(this, view);
        mUnbinders.add(unbinder);
        return unbinder;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Iterator<Unbinder> i = mUnbinders.iterator();
        while (i.hasNext()) {
            i.next().unbind();
            i.remove();
        }
    }

    @Override
    public void showError(String msg) {
        SpannableString str = new SpannableString(msg);
        str.setSpan(new ForegroundColorSpan(Color.RED), 0, msg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Toast t = Toast.makeText(getContext(), str, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }

}
