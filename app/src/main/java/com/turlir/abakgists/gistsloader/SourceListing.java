package com.turlir.abakgists.gistsloader;

import com.turlir.abakgists.base.loader.Pager;
import com.turlir.abakgists.base.loader.Window;
import com.turlir.abakgists.base.loader.server.LoadableItem;
import com.turlir.abakgists.base.loader.server.Serverable;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.api.data.GistMapper;
import com.turlir.abakgists.api.data.ListGistMapper;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class SourceListing implements Pager<GistModel>, Serverable {

    private final Repository mRepo;
    private final ListGistMapper.Local mTransformer = new ListGistMapper.Local(new GistMapper.Local());

    public SourceListing(Repository repo) {
        mRepo = repo;
    }

    @Override
    public Flowable<List<GistModel>> currentPage(Window w) {
        return mRepo.database(w.count(), w.start())
                .map(mTransformer);
    }

    @Override
    public Single<Integer> loadFromServer(LoadableItem page) {
        return mRepo.fromServerToDatabase(page.firstAxis(), page.secondAxis());
    }

    @Override
    public Single<Integer> update(LoadableItem page) {
        return mRepo.reloadAllGist(page.firstAxis(), page.secondAxis());
    }
}
