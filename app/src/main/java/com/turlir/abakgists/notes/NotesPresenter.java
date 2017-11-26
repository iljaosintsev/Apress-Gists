package com.turlir.abakgists.notes;

import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.notes.view.NotesFragment;

import java.util.List;

public class NotesPresenter extends BasePresenter<NotesFragment> {

    private final NotesInteractor mInteractor;

    public NotesPresenter(NotesInteractor interactor) {
        mInteractor = interactor;
    }

    public void loadNotes() {
        addSubscription(mInteractor.requestWithNotes()
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
