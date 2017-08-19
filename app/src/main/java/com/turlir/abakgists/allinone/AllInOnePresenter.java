package com.turlir.abakgists.allinone;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.GistsTable;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class AllInOnePresenter extends BasePresenter<AllInOneActivity> {

    private final StorIOSQLite mRepo;
    private final Query.CompleteBuilder mSearchBuilder;

    public AllInOnePresenter(StorIOSQLite repo) {
        mRepo = repo;

        mSearchBuilder = Query.builder()
                .table(GistsTable.GISTS)
                .where(GistsTable.DESC + " LIKE ?" + " OR " + GistsTable.NOTE + " LIKE ?");
    }

    void connectSearch(Observable<String> searchObs) {

        Subscription subs = searchObs
                .switchMap(new Func1<String, Observable<List<GistLocal>>>() {
                    @Override
                    public Observable<List<GistLocal>> call(String query) {
                        return mRepo.get()
                                .listOfObjects(GistLocal.class)
                                .withQuery(mSearchBuilder.whereArgs(query, query).build())
                                .prepare()
                                .asRxObservable();
                    }
                })
                .map(new Func1<List<GistLocal>, List<String>>() {
                    @Override
                    public List<String> call(List<GistLocal> gistLocals) {
                        List<String> res = new ArrayList<>(gistLocals.size());
                        for (GistLocal item : gistLocals) {
                            res.add(String.format("%s\n%s", item.description, item.note));
                        }
                        return res;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Handler<List<String>>() {
                    @Override
                    public void onNext(List<String> strings) {
                        if (getView() != null) {
                            getView().onSearchResults(strings);
                        }
                    }
                });

        addSubscription(subs);
    }
}
