package com.turlir.abakgists.model;


import com.turlir.abakgists.allgists.TypesFactory;
import com.turlir.abakgists.allgists.ViewModel;

public class LoadingModel extends ViewModel {

    public final int count;

    public LoadingModel(int count) {
        this.count = count;
    }

    @Override
    public int type(TypesFactory factory) {
        return factory.type(this);
    }

}
