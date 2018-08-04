package com.turlir.abakgists.notes;

import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.api.data.GistMapper;
import com.turlir.abakgists.api.data.ListGistMapper;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

import io.reactivex.Flowable;

public class NotesInteractor {

    private final Repository mRepo;
    private final ListGistMapper.Local mMapper;

    public NotesInteractor(Repository repo) {
        mRepo = repo;
        mMapper = new ListGistMapper.Local(new GistMapper.Local());
        mMapper.setLocal(true);
    }

    Flowable<List<GistModel>> requestWithNotes() {
        return mRepo.notes()
                .map(mMapper);
    }

}
