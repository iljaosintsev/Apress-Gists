package com.turlir.abakgists.model;


import com.turlir.abakgists.allgists.view.ViewModel;

public class LoadingModel implements ViewModel {

    public final int count;

    public LoadingModel(int count) {
        this.count = count; // R.layout.inline_loading
    }
}
