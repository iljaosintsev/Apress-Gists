package com.turlir.abakgists.notes;

import com.turlir.abakgists.allgists.ModelRequester;
import com.turlir.abakgists.base.App;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.notes.view.NotesFragment;

import java.util.List;

import javax.inject.Inject;

public class NotesPresenter extends BasePresenter<NotesFragment> {

    @Inject
    ModelRequester _interactor;

    public NotesPresenter() {
        App.getComponent().inject(this);
    }

    public void loadNotes() {
        addSubscription(_interactor.requestWithNotes()
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
