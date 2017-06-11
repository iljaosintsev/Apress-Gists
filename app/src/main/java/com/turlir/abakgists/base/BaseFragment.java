package com.turlir.abakgists.base;


import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

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
        if (getView() != null) {
            Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
        }
    }

}
