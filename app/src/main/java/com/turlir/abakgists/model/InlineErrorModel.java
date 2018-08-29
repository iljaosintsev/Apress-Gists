package com.turlir.abakgists.model;


import android.view.View;

import com.turlir.abakgists.allgists.view.ViewModel;

public class InlineErrorModel implements ViewModel {

    public final View.OnClickListener clicker;

    public InlineErrorModel(View.OnClickListener clicker) {
        this.clicker = clicker;
    }
}
