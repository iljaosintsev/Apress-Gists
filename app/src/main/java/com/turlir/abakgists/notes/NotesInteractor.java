package com.turlir.abakgists.notes;

import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.api.data.GistMapper;
import com.turlir.abakgists.api.data.ListGistMapper;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

import rx.Observable;

public class NotesInteractor {

    private final Repository mRepo;

    public NotesInteractor(Repository repo) {
        mRepo = repo;
    }

    Observable<List<GistModel>> requestWithNotes() {
        ListGistMapper.Local mapper = new ListGistMapper.Local(new GistMapper.Local());
        mapper.setLocal(true);
        return mRepo.loadWithNotes()
                .map(mapper);
    }

}
