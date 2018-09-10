package com.turlir.abakgists.gist;

import com.arellomobile.mvp.MvpView;
import com.turlir.abakgists.model.GistModel;

public interface GistView extends MvpView {

    void onLoadSuccess(GistModel model);

    void onLoadFailure();

    void updateSuccess();

    void deleteSuccess();

    void deleteFailure();
}
