package com.turlir.abakgists.api;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.api.data.ListGistJsonToLocalMapper;
import com.turlir.abakgists.model.GistsTable;

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
    public Observable<PutResults<GistLocal>> reloadGists() {
        return loadGistsFromServer(0)
                .flatMap(new Func1<List<GistLocal>, Observable<PutResults<GistLocal>>>() {
                    @Override
                    public Observable<PutResults<GistLocal>> call(List<GistLocal> gistModels) {
                        return clearCacheAndPut(gistModels);
                    }
                });
    }

    /**
     * Получить данные с сервера или локальной базы (приоритетнее)
     *
     * @return список элементов
     */
    public Observable<List<GistLocal>> loadGists() {
        return mDatabase.get()
                .listOfObjects(GistLocal.class)
                .withQuery(GistsTable.REQUEST_ALL)
                .prepare()
                .asRxObservable();
    }

    public Observable<List<GistLocal>> loadGistsFromServerAndPutCache(int currentSize) {
        return loadGistsFromServer(currentSize)
                .flatMap(new Func1<List<GistLocal>, Observable<PutResults<GistLocal>>>() {
                    @Override
                    public Observable<PutResults<GistLocal>> call(List<GistLocal> gists) {
                        return putGistsToCache(gists);
                    }
                }, new Func2<List<GistLocal>, PutResults<GistLocal>, List<GistLocal>>() {
                    @Override
                    public List<GistLocal> call(List<GistLocal> gistModels, PutResults<GistLocal> gistModelPutResults) {
                        return gistModels;
                    }
                });
    }

    ///
    /// private
    ///

    private Observable<List<GistLocal>> loadGistsFromServer(int currentSize) {
        int page = Math.round(currentSize / PAGE_SIZE) + 1;
        return mClient.publicGist(page)
                .doOnNext(new LagSideEffect(2500))
                .map(new ListGistJsonToLocalMapper());
    }

    private Observable<PutResults<GistLocal>> putGistsToCache(List<GistLocal> gists) {
        return mDatabase.put()
                .objects(gists)
                .prepare()
                .asRxObservable();
    }

    private Observable<PutResults<GistLocal>> clearCacheAndPut(final List<GistLocal> gists) {
        return mDatabase.delete()
                .byQuery(GistsTable.DELETE_ALL)
                .prepare()
                .asRxObservable()
                .flatMap(new Func1<DeleteResult, Observable<PutResults<GistLocal>>>() {
                    @Override
                    public Observable<PutResults<GistLocal>> call(DeleteResult deleteResult) {
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
