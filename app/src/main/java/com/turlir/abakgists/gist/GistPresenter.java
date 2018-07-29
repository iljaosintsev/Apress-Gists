package com.turlir.abakgists.gist;

import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.GistModel;

public class GistPresenter extends BasePresenter<GistActivity> {

    private final EqualsSolver mSolver;

    public GistModel content;

    public GistPresenter(EqualsSolver solver) {
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
        // put in database
        content = now;
    }

}
