package com.turlir.abakgists.notes;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.GistsTable;

import java.util.List;

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
                .listOfObjects(GistModel.class)
                .withQuery(
                        Query.builder()
                                .table(GistsTable.GISTS)
                                .where("note IS NOT NULL AND note != \"\"")
                                .build()
                )
                .prepare()
                .asRxObservable()
                .compose(this.<List<GistModel>>defaultScheduler())
                .subscribe(new Handler<List<GistModel>>() {
                    @Override
                    public void onNext(List<GistModel> gist) {
                        if (getView() != null) {
                            if (gist.size() > 0) {
                                if (gist.size() > mLastSize) {
                                    gist = gist.subList(mLastSize, gist.size());
                                    getView().onNotesLoaded(gist);
                                } else {
                                    getView().onNotesDeleted();
                                }
                                mLastSize = gist.size();

                            } else if (mLastSize != 0) {
                                getView().onNotesDeleted();
                            }
                        }

                    }
                }));
    }

}
