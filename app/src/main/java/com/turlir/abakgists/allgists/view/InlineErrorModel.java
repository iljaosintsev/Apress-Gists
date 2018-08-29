package com.turlir.abakgists.allgists.view;


import android.view.View;

class InlineErrorModel extends ViewModel {

    final View.OnClickListener clicker;

    InlineErrorModel(View.OnClickListener clicker) {
        this.clicker = clicker;
    }
}
