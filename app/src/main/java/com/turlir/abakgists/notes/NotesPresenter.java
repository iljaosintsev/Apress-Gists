package com.turlir.abakgists.notes;

import com.turlir.abakgists.allgists.ModelRequester;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.notes.view.NotesFragment;

import java.util.List;

public class NotesPresenter extends BasePresenter<NotesFragment> {

    private final ModelRequester mRequester;

    public NotesPresenter(Repository repo) {
        mRequester = new ModelRequester(repo);
    }

    public void loadNotes() {
        addSubscription(mRequester.requestWithNotes()
                .compose(this.<List<GistModel>>defaultScheduler())
                .subscribe(new Handler<List<GistModel>>() {
                    @Override
                    public void onNext(final List<GistModel> gist) {
                        if (getView() == null) return;

                        getView().onNotesLoaded(gist);
                    }
                }));
    }

}
