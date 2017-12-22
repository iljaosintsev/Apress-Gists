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

public class AllInOnePresenter extends BasePresenter<AllInOneActivity> {

    private final StorIOSQLite mRepo;
    private final Query.CompleteBuilder mSearchBuilder;

    private Subscription mSearching;

    public AllInOnePresenter(StorIOSQLite repo) {
        mRepo = repo;

        mSearchBuilder = Query.builder()
                .table(GistsTable.GISTS)
                .where(GistsTable.DESC + " LIKE ?" + " OR " + GistsTable.NOTE + " LIKE ?");
    }

    void connectSearch(Observable<String> searchObs) {
        mSearching = searchObs
                .switchMap(query -> {
                    query = "%" + query + "%";
                    return mRepo.get()
                            .listOfObjects(GistLocal.class)
                            .withQuery(mSearchBuilder.whereArgs(query, query).build())
                            .prepare()
                            .asRxObservable();
                })
                .map(gistLocals -> {
                    List<String> res = new ArrayList<>(gistLocals.size());
                    for (GistLocal item : gistLocals) {
                        res.add(String.format("%s\n%s", item.description, item.note));
                    }
                    return res;
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

        addSubscription(mSearching);
    }

    void detachSearch() {
        if (mSearching != null) {
            removeSubscription(mSearching);
        }
    }

}
