package com.turlir.abakgists.allgists.view;

import com.turlir.abakgists.base.BaseView;
import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

public interface GistListView extends ErrorInterpreter, BaseView {

    void onGistLoaded(List<GistModel> value, boolean resetForward, boolean resetBackward);

    void toBlockingLoad(boolean visible);

    void inlineLoad(boolean visible);
}
