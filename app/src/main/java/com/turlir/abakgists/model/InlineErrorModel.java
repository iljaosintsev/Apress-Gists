package com.turlir.abakgists.model;


import android.view.View;

public class InlineErrorModel implements InterfaceModel {

    public final View.OnClickListener clicker;

    public InlineErrorModel(View.OnClickListener clicker) {
        this.clicker = clicker;
    }
}
