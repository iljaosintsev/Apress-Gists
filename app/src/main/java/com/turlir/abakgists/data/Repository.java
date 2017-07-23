package com.turlir.abakgists.data;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.GistsTable;
import com.turlir.abakgists.model.ListGistToModelMapper;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;


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
     *
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
     *
     * @return список элементов
     */
    public Observable<List<GistModel>> loadGists() {
        return mDatabase.get()
                .listOfObjects(GistModel.class)
                .withQuery(GistsTable.REQUEST_ALL)
                .prepare()
                .asRxObservable();
    }

    public Observable<List<GistModel>> loadGistsFromServerAndPutCache(int currentSize) {
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

    ///
    /// private
    ///

    private Observable<List<GistModel>> loadGistsFromServer(int currentSize) {
        int page = Math.round(currentSize / PAGE_SIZE) + 1;
        return mClient
                .publicGist(page)
                .doOnNext(new LagSideEffect(2500))
                .map(new ListGistToModelMapper())
                .doOnNext(new Action1<List<GistModel>>() {
                    @Override
                    public void call(List<GistModel> gistModels) {
                    }
                });
    }

    private Observable<PutResults<GistModel>> putGistsToCache(List<GistModel> gists) {
        return mDatabase.put()
                .objects(gists)
                .prepare()
                .asRxObservable();
    }

    private Observable<PutResults<GistModel>> clearCacheAndPut(final List<GistModel> gists) {
        return mDatabase.delete()
                .byQuery(GistsTable.DELETE_ALL)
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
