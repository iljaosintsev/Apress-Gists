package com.turlir.abakgists.api;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.api.data.GistMapper;
import com.turlir.abakgists.api.data.ListGistMapper;
import com.turlir.abakgists.model.GistsTable;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

public class Repository {

    private final ApiClient mClient;
    private final StorIOSQLite mDatabase;

    private final ListGistMapper.Json mTransformer;

    public Repository(ApiClient client, StorIOSQLite base) {
        mClient = client;
        mDatabase = base;

        mTransformer = new ListGistMapper.Json(new GistMapper.Json());
    }

    /**
     * Перезагрузить данные с сервера
     * При этом очищается локальная БД
     *
     * @return сведения о вновь добавленных элементах
     */
    public Observable<PutResults<GistLocal>> reload() {
        return loadFromServer(1)
                .flatMap(new Func1<List<GistLocal>, Observable<PutResults<GistLocal>>>() {
                    @Override
                    public Observable<PutResults<GistLocal>> call(List<GistLocal> gistModels) {
                        return clearCacheAndPut(gistModels);
                    }
                });
    }

    /**
     * Получить данные из локальной базы
     *
     * @return список элементов
     */
    public Observable<List<GistLocal>> load() {
        return mDatabase.get()
                .listOfObjects(GistLocal.class)
                .withQuery(GistsTable.REQUEST_ALL)
                .prepare()
                .asRxObservable();
    }

    public Observable<List<GistLocal>> loadWithNotes() {
        return mDatabase.get()
                .listOfObjects(GistLocal.class)
                .withQuery(
                        Query.builder()
                                .table(GistsTable.GISTS)
                                .where(GistsTable.NOTE + " NOT NULL AND " + GistsTable.NOTE + " != \"\"")
                                .build()
                )
                .prepare()
                .asRxObservable();
    }

    /**
     * Скачивает очередную страницу с сервера и сохраняет ее в БД
     *
     * @param page номер загружаемой страницы, больше 1
     * @return сохраненные в БД элементы
     */
    public Observable<List<GistLocal>> server(int page) {
        return loadFromServer(page)
                .flatMap(new Func1<List<GistLocal>, Observable<PutResults<GistLocal>>>() {
                    @Override
                    public Observable<PutResults<GistLocal>> call(List<GistLocal> gists) {
                        return putToCache(gists);
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

    private Observable<List<GistLocal>> loadFromServer(int page) {
        if (page < 1) throw new IllegalArgumentException();
        return mClient.publicGist(page)
                .doOnNext(new LagSideEffect(2500))
                .map(mTransformer);
    }

    private Observable<PutResults<GistLocal>> putToCache(List<GistLocal> gists) {
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
                        return putToCache(gists);
                    }
                });
    }

    /**
     * Эффект задержки сети на {@code millis} миллисекунд
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
