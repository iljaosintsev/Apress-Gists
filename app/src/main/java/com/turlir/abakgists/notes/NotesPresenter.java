package com.turlir.abakgists.notes;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.Gist;

import java.util.List;

import rx.functions.Func1;

public class NotesPresenter extends BasePresenter<NotesFragment> {

    private StorIOSQLite mDatabase;
    private int mLastSize;

    public NotesPresenter(StorIOSQLite db) {
        mDatabase = db;
    }

    @Override
    public void detach() {
        super.detach();
        mLastSize = 0;
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
                .map(new Func1<List<Gist>, List<Gist>>() {
                    @Override
                    public List<Gist> call(List<Gist> gist) {
                        if (gist.size() > mLastSize) {
                            return gist.subList(mLastSize, gist.size());
                        }
                        return gist;
                    }
                })
                .compose(this.<List<Gist>>defaultScheduler())
                .subscribe(new Handler<List<Gist>>() {
                    @Override
                    public void onNext(List<Gist> gist) {
                        mLastSize = gist.size();
                        if (getView() != null) {
                            if (gist.size() > 0) {
                                getView().onNotesLoaded(gist);
                            }
                        }
                    }
                }));
    }

}
