package com.turlir.abakgists;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.Gist;

import java.util.List;

import rx.Observable;

public class NotesPresenter extends BasePresenter<NotesFragment> {

    private StorIOSQLite mDatabase;

    public NotesPresenter(StorIOSQLite db) {
        mDatabase = db;
    }

    void loadNotes() {
        Observable<List<Gist>> notes = mDatabase.get()
                .listOfObjects(Gist.class)
                .withQuery(
                        Query.builder()
                                .table("gists")
                                .where("note IS NOT NULL AND note != \"\"")
                                .build()
                )
                .prepare()
                .asRxObservable();

        notes.subscribe(new Handler<List<Gist>>() {
            @Override
            public void onNext(List<Gist> gist) {
                if (getView() != null) {
                    getView().onNotesLoaded(gist);
                }
            }
        });
    }

}
