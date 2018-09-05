package com.turlir.abakgists.allgists.view;

import com.turlir.abakgists.base.BaseView;
import com.turlir.abakgists.model.ErrorModel;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.LoadingModel;

import java.util.List;

public interface GistListView extends BaseView {

    void onGistLoaded(List<GistModel> value, boolean resetForward, boolean resetBackward);

    void toBlockingLoad(boolean visible);

    void inlineLoad(LoadingModel model);

    void removeInlineLoad();

    // like a ErrorInterpreter

    /**
     * Не блокирующая ошибка, когда контент уже есть
     * @param msg описание ситуации
     */
    void nonBlockingError(String msg);

    /**
     * Случайная ошибка, когда определить дальнейшие действия нельзя. Например NPE
     * @param msg описание ситуации
     */
    void alertError(String msg);

    /**
     * Блокирующая, когда данных нет или дальнейшая работа невозможна
     * @param model описание ситуации
     */
    void blockingError(ErrorModel model);
}