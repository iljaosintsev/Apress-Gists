package com.turlir.abakgists.notes;

import com.turlir.abakgists.R;
import com.turlir.abakgists.allgists.NotesInteractor;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.base.BaseView;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.notes.view.NotesFragment;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import timber.log.Timber;

public class NotesPresenter extends BasePresenter<NotesFragment> {

    private final NotesInteractor mInteractor;

    public NotesPresenter(NotesInteractor interactor) {
        mInteractor = interactor;
    }

    public void loadNotes() {
        addSubscription(mInteractor.requestWithNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<GistModel>>() {
                    @Override
                    public void onNext(List<GistModel> gists) {
                        if (getView() !=null) {
                            Timber.d("detected %d items with note", gists.size());
                            getView().onNotesLoaded(gists);
                        }
                    }
                    @Override
                    public void onError(Throwable t) {
                        Timber.e(t);
                        BaseView view = getView();
                        if (view != null) {
                            CharSequence msg = view.getContext().getString(R.string.error_general);
                            view.showError(msg.toString());
                        }
                    }
                    @Override
                    public void onComplete() {
                        Timber.v("notes from database subscription completed");
                    }
                }));
    }

}
