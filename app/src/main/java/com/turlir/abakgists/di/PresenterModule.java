package com.turlir.abakgists.di;


import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.turlir.abakgists.allgists.AllGistsPresenter;
import com.turlir.abakgists.network.Repository;
import com.turlir.abakgists.notes.NotesPresenter;
import com.turlir.abakgists.network.ApiClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppModule.class)
public class PresenterModule {

    @Provides
    @Singleton
    public AllGistsPresenter provideAllGistsPresenter(Repository repo) {
        return new AllGistsPresenter(repo);
    }

    @Provides
    @Singleton
    public NotesPresenter provideNotesPresenter(StorIOSQLite database) {
        return new NotesPresenter(database);
    }

}
