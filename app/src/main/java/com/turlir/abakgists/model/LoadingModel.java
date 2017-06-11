package com.turlir.abakgists.model;


import com.turlir.abakgists.allgists.TypesFactory;
import com.turlir.abakgists.allgists.ViewModel;

public class LoadingModel extends ViewModel {

    @Override
    public int type(TypesFactory factory) {
        return factory.type(this);
    }

}
