package com.turlir.abakgists.allgists;

import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.api.data.GistMapper;
import com.turlir.abakgists.api.data.ListGistMapper;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

import io.reactivex.Flowable;

public class NotesInteractor {

    private final Repository mRepo;
    private final ListGistMapper.Local mTransformer = new ListGistMapper.Local(new GistMapper.Local());

    public NotesInteractor(Repository repo) {
        mRepo = repo;
    }

    public Flowable<List<GistModel>> requestWithNotes() {
        return mRepo.notes()
                .map(gistLocals -> {
                    boolean tmp = mTransformer.isLocal();
                    mTransformer.setLocal(true);
                    List<GistModel> res = mTransformer.apply(gistLocals);
                    mTransformer.setLocal(tmp);
                    return res;
                });
    }

}
