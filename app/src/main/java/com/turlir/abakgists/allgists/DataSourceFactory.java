package com.turlir.abakgists.allgists;

import com.turlir.abakgists.allgists.combination.ListManipulator;
import com.turlir.abakgists.allgists.loader.Range;
import com.turlir.abakgists.allgists.loader.WindowedRepository;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.model.GistModel;

public class DataSourceFactory {

    private final Repository mRepo;

    public DataSourceFactory(Repository repo) {
        mRepo = repo;
    }

    GistListInteractor create(Range range) {
        return new GistListInteractor(mRepo, range);
    }

    GistLoader create(WindowedRepository<GistModel> interactor, ListManipulator<GistModel> callback) {
        return new GistLoader(interactor, callback);
    }
}
