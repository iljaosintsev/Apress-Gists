package com.turlir.abakgists.gist;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.turlir.abakgists.model.GistModel;

public interface GistView extends MvpView {

    @StateStrategyType(SingleStateStrategy.class)
    void onLoadSuccess(GistModel model);

    @StateStrategyType(SingleStateStrategy.class)
    void onLoadFailure();

    @StateStrategyType(SingleStateStrategy.class)
    void updateSuccess();

    @StateStrategyType(SingleStateStrategy.class)
    void deleteSuccess();

    @StateStrategyType(SkipStrategy.class)
    void deleteFailure();
}
