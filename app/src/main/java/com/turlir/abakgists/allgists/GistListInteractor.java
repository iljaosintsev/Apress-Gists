package com.turlir.abakgists.allgists;

import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.api.data.GistMapper;
import com.turlir.abakgists.api.data.ListGistMapper;
import com.turlir.abakgists.model.GistModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import timber.log.Timber;

public class GistListInteractor {

    static final int IGNORE_SIZE = -1;
    private static final float PAGE_SIZE = 30;

    private final Repository mRepo;
    private final List<GistModel> mData;
    private final GistMapper.Local mTransformer;

    public GistListInteractor(Repository repo) {
        mRepo = repo;
        mData = new ArrayList<>();
        mTransformer = new GistMapper.Local();
    }

    public Observable<List<GistModel>> requestWithNotes() {
        ListGistMapper.Local mapper = new ListGistMapper.Local(new GistMapper.Local());
        mapper.setLocal(true);
        return mRepo.loadWithNotes()
                .map(mapper);
    }

    List<GistModel> accumulator() {
        return new ArrayList<>(mData); // shadow copy
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

        return mRepo.load()
                .flatMap(new Func1<List<GistLocal>, Observable<List<GistLocal>>>() {
                    @Override
                    public Observable<List<GistLocal>> call(List<GistLocal> gistModels) {
                        if (gistModels.size() < size + 1) {
                            mTransformer.setLocal(false);
                            int page = Math.round(size / PAGE_SIZE) + 1;
                            return mRepo.server(page)
                                    .skip(1);
                        } else {
                            return Observable.just(gistModels);
                        }
                    }
                })
                .map(new Func1<List<GistLocal>, List<GistModel>>() {
                    @Override
                    public List<GistModel> call(List<GistLocal> gistLocals) {
                        final int originCacheSize = mData.size();

                        for (int i = 0; i < gistLocals.size(); i++) {
                            final GistLocal item = gistLocals.get(i);

                            if (i + 1 > originCacheSize) { // новые данные в БД
                                GistModel m = mTransformer.call(item);
                                mData.add(m);

                            } else { // обновление БД
                                GistModel cache = mData.get(i);

                                boolean changed =
                                        !cache.description.equals(item.description) ||
                                        !cache.note.equals(item.note);

                                if (changed) {
                                    Timber.d("%s recreated", cache);
                                    mTransformer.setLocal(mData.get(i).isLocal);
                                    mData.set(i, mTransformer.call(item));
                                    // только один элемент из набора может измениться с обновлением
                                    i = originCacheSize;
                                }
                            }
                        }

                        return mData;
                    }
                });
    }

    /**
     * Обнвляет данные. Скачивает свежие данные с сервра, перезаписывает локальную базу
     *
     * @return сведения о записанных элементах
     */
    Observable<PutResults<GistLocal>> update() {
        mTransformer.setLocal(false);
        return mRepo.reload()
                .doOnNext(new Action1<PutResults<GistLocal>>() {
                    @Override
                    public void call(PutResults<GistLocal> gistLocalPutResults) {
                        // сохраняем кеш в RAM до успешного завершения сетевого запроса
                        mData.clear();
                    }
                });
    }
}
