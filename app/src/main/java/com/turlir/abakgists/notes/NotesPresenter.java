package com.turlir.abakgists.notes;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.Gist;

import java.util.List;

public class NotesPresenter extends BasePresenter<NotesFragment> {

    private StorIOSQLite mDatabase;

    public NotesPresenter(StorIOSQLite db) {
        mDatabase = db;
    }

    void loadNotes() {
        addSubscription(mDatabase.get()
                .listOfObjects(Gist.class)
                .withQuery(
                        Query.builder()
                                .table("gists")
                                .where("note IS NOT NULL AND note != \"\"")
                                .build()
                )
                .prepare()
                .asRxObservable()
                .compose(this.<List<Gist>>defaultScheduler())
                .subscribe(new Handler<List<Gist>>() {
                    @Override
                    public void onNext(List<Gist> gist) {
                        if (getView() != null) {
                            getView().onNotesLoaded(gist);
                        }
                    }
                }));
    }

}
