package com.turlir.abakgists.gist;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.api.data.GistLocalStorIOSQLitePutResolver;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.GistModel;

public class GistPresenter extends BasePresenter<GistActivity> {

    private static final GistLocalStorIOSQLitePutResolver UPDATE_RESOLVER
            = new GistLocalStorIOSQLitePutResolver();

    private final StorIOSQLite mDatabase;
    private final EqualsSolver mSolver;

    public GistModel content;

    public GistPresenter(StorIOSQLite database, EqualsSolver solver) {
        mDatabase = database;
        mSolver = solver;
    }

    void attach(GistActivity view, GistModel model) {
        super.attach(view);
        content = model;
    }

    boolean isChange(String desc, String note) {
        GistModel now = new GistModel(content, desc, note);
        return mSolver.solveModel(content, now);
    }

    void transact(String desc, String note) {
        GistModel now = new GistModel(content, desc, note);
        GistLocal local = new GistLocal(now.id, now.url, now.created, now.description, now.note,
                now.ownerLogin, now.ownerAvatarUrl);
        mDatabase.put()
                .object(local)
                .withPutResolver(UPDATE_RESOLVER)
                .prepare()
                .executeAsBlocking();
        content = now;
    }

}
