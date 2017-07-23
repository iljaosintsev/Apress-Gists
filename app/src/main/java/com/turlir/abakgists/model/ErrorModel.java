package com.turlir.abakgists.model;


import com.turlir.abakgists.allgists.view.TypesFactory;
import com.turlir.abakgists.allgists.view.ViewModel;

public class ErrorModel extends ViewModel {

    public final String description;

    public ErrorModel(String desc) {
        description = desc;
    }

    @Override
    public int type(TypesFactory factory) {
        return factory.type(this);
    }

}
