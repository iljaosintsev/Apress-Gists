package com.turlir.abakgists.allgists;

import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.api.data.GistMapper;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

import rx.Observable;

public class GistListInteractor {

    private static final float PAGE_SIZE = 30;

    private final Repository mRepo;
    private final GistMapper.Local mTransformer;

    public GistListInteractor(Repository repo) {
        mRepo = repo;

        mTransformer = new GistMapper.Local();
    }

    /**
     * Извлекает данные из кеша, при необходимости загружает их с сервера
     *
     * @param size количество уже загруженных элементов
     * @return элементы представления
     */
    Observable<List<GistModel>> request(final int size) {
        if (size == 0) {
            mTransformer.setLocal(true);
        }
        return null;
    }
    /**
     * Обнвляет данные. Скачивает свежие данные с сервра, перезаписывает локальную базу
     *
     * @return сведения о записанных элементах
     */
    Observable<List<GistLocal>> update() {
        return null;
    }
}
