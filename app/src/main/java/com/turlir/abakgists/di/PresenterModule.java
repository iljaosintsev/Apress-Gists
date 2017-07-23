package com.turlir.abakgists.di;


import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.turlir.abakgists.allgists.AllGistsPresenter;
import com.turlir.abakgists.allgists.ModelRequester;
import com.turlir.abakgists.data.Repository;
import com.turlir.abakgists.notes.NotesPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppModule.class)
public class PresenterModule {

    @Provides
    // @Singleton
    public ModelRequester provideModelRequester(Repository repo) {
        return new ModelRequester(repo);
    }

    @Provides
    @Singleton
    public AllGistsPresenter provideAllGistsPresenter(ModelRequester req) {
        return new AllGistsPresenter(req);
    }

    @Provides
    @Singleton
    public NotesPresenter provideNotesPresenter(StorIOSQLite database) {
        return new NotesPresenter(database);
    }

}
