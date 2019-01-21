package com.turlir.abakgists.all;

import com.arellomobile.mvp.MvpView;

import java.util.List;

public interface AllInOneView extends MvpView {

    void onSearchResults(List<String> strings);
}
