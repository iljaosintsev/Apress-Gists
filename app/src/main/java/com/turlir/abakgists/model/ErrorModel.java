package com.turlir.abakgists.model;


import com.turlir.abakgists.allgists.view.ViewModel;

public class ErrorModel implements ViewModel {

    public final String description;

    public ErrorModel(String desc) {
        description = desc;
    }

}
