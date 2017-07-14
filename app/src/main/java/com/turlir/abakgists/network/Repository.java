package com.turlir.abakgists.network;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.ListGistToModelMapper;
import com.turlir.abakgists.model.GistsTable;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;


public class Repository {

    private static final float PAGE_SIZE = 30;

    private ApiClient mClient;
    private StorIOSQLite mDatabase;

    public Repository(ApiClient client, StorIOSQLite base) {
        mClient = client;
        mDatabase = base;
    }

    /**
     * Перезагрузить данные с сервера
     * При этом очищается локальная БД
     * @return количество вновь добавленных элементов (может быть проигнорировано)
     */
    public Observable<PutResults<GistModel>> reloadGists() {
        return loadGistsFromServer(0)
                .flatMap(new Func1<List<GistModel>, Observable<PutResults<GistModel>>>() {
                    @Override
                    public Observable<PutResults<GistModel>> call(List<GistModel> gistModels) {
                        return clearCacheAndPut(gistModels);
                    }
                });
    }

    /**
     * Получить данные с сервера или локальной базы (приоритетнее)
     * @param size количество элементов, добавленных в список (для пагинации) >= 0
     * @return список элементов
     */
    public Observable<List<GistModel>> loadGists(final int size) {
        return mDatabase.get()
                .listOfObjects(GistModel.class)
                .withQuery(
                        Query.builder()
                                .table(GistsTable.GISTS)
                                .build()
                )
                .prepare()
                .asRxObservable()
                .switchMap(new Func1<List<GistModel>, Observable<List<GistModel>>>() {
                    @Override
                    public Observable<List<GistModel>> call(List<GistModel> gistModels) {
                        if (gistModels.size() < size + 1) {
                            return loadGistsFromServerAndPutCache(size);
                        } else {
                            return Observable.just(gistModels);
                        }
                    }
                });
    }

    ///
    /// private
    ///

    private Observable<List<GistModel>> loadGistsFromServerAndPutCache(int currentSize) {
        return loadGistsFromServer(currentSize)
                .flatMap(new Func1<List<GistModel>, Observable<PutResults<GistModel>>>() {
                    @Override
                    public Observable<PutResults<GistModel>> call(List<GistModel> gists) {
                        return putGistsToCache(gists);
                    }
                }, new Func2<List<GistModel>, PutResults<GistModel>, List<GistModel>>() {
                    @Override
                    public List<GistModel> call(List<GistModel> gistModels, PutResults<GistModel> gistModelPutResults) {
                        return gistModels;
                    }
                });
    }

    private Observable<List<GistModel>> loadGistsFromServer(int currentSize) {
        int page = Math.round(currentSize / PAGE_SIZE) + 1;
        return mClient
                .publicGist(page)
                .doOnNext(new LagSideEffect(2500))
                .subscribeOn(Schedulers.io())
                .map(new ListGistToModelMapper());
    }

    private Observable<PutResults<GistModel>> putGistsToCache(List<GistModel> gists) {
        return mDatabase.put()
                .objects(gists)
                .prepare()
                .asRxObservable();
    }

    private Observable<PutResults<GistModel>> clearCacheAndPut(final List<GistModel> gists) {
        return mDatabase.delete()
                .byQuery(
                        DeleteQuery
                                .builder()
                                .table(GistsTable.GISTS)
                                .build()
                )
                .prepare()
                .asRxObservable()
                .flatMap(new Func1<DeleteResult, Observable<PutResults<GistModel>>>() {
                    @Override
                    public Observable<PutResults<GistModel>> call(DeleteResult deleteResult) {
                        return putGistsToCache(gists);
                    }
                });
    }

    /**
     * Like a network lag in {@code millis} millisecond
     */
    private static class LagSideEffect implements Action1<Object> {

        private final int mLag;

        private LagSideEffect(int millis) {
            mLag = millis;
        }

        @Override
        public void call(Object data) {
            try {
                Thread.sleep(mLag);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
