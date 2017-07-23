package com.turlir.abakgists.notes;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.GistsTable;
import com.turlir.abakgists.notes.view.NotesFragment;

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

    public void loadNotes() {
        addSubscription(mDatabase.get()
                .listOfObjects(GistModel.class)
                .withQuery(
                        Query.builder()
                                .table(GistsTable.GISTS)
                                .where(GistsTable.NOTE + " NOT NULL AND " + GistsTable.NOTE + " != \"\"")
                                .build()
                )
                .prepare()
                .asRxObservable()
                .compose(this.<List<GistModel>>defaultScheduler())
                .subscribe(new Handler<List<GistModel>>() {
                    @Override
                    public void onNext(final List<GistModel> gist) {
                        if (getView() == null) return;

                        if (gist.size() > 0) {
                            if (gist.size() > mLastSize) { // больше
                                List<GistModel> tmp = gist.subList(mLastSize, gist.size());
                                getView().onNotesLoaded(tmp);
                            } else {
                                if (gist.size() == mLastSize) {
                                    for (int i = 0; i < mLastSize; i++) {
                                        getView().onNotesDeleted();
                                    }
                                    getView().onNotesLoaded(gist);
                                } else {
                                    getView().onNotesDeleted();
                                }
                            }
                            mLastSize = gist.size();

                        } else if (mLastSize != 0) {
                            getView().onNotesDeleted();
                        }

                    }
                }));
    }

}
