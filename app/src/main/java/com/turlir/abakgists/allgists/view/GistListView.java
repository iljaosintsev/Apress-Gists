package com.turlir.abakgists.allgists.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.turlir.abakgists.model.ErrorModel;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.LoadingModel;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface GistListView extends MvpView {

    void onGistLoaded(List<GistModel> value, boolean resetForward, boolean resetBackward);

    void toBlockingLoad(boolean visible);

    @StateStrategyType(value = AddToEndSingleStrategy.class, tag = "inlineLoad")
    void inlineLoad(LoadingModel model);

    @StateStrategyType(value = AddToEndSingleStrategy.class, tag = "inlineLoad")
    void removeInlineLoad();

    // like a ErrorInterpreter

    /**
     * Не блокирующая ошибка, когда контент уже есть
     * @param msg описание ситуации
     */
    @StateStrategyType(value = AddToEndSingleStrategy.class, tag = "error")
    void nonBlockingError(String msg);

    /**
     * Случайная ошибка, когда определить дальнейшие действия нельзя. Например NPE
     * @param msg описание ситуации
     */
    @StateStrategyType(value = AddToEndSingleStrategy.class, tag = "error")
    void alertError(String msg);

    /**
     * Блокирующая, когда данных нет или дальнейшая работа невозможна
     * @param model описание ситуации
     */
    @StateStrategyType(value = SingleStateStrategy.class)
    void blockingError(ErrorModel model);
}