package com.turlir.abakgists.di;


import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.turlir.abakgists.allgists.AllGistsPresenter;
import com.turlir.abakgists.allgists.GistListInteractor;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.gist.EqualsSolver;
import com.turlir.abakgists.gist.GistPresenter;
import com.turlir.abakgists.notes.NotesInteractor;
import com.turlir.abakgists.notes.NotesPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppModule.class)
public class PresenterModule {

    @Provides
    GistListInteractor provideGistListInteractor(Repository repo) {
        return new GistListInteractor(repo);
    }

    @Provides
    NotesInteractor provideNotesInteractor(Repository repo) {
        return new NotesInteractor(repo);
    }

    @Provides
    @Singleton
    AllGistsPresenter provideAllGistsPresenter() {
        return new AllGistsPresenter();
    }

    @Provides
    @Singleton
    NotesPresenter provideNotesPresenter() {
        return new NotesPresenter();
    }

    @Provides
    @Singleton
    GistPresenter provideGistPresenter(StorIOSQLite base) {
        return new GistPresenter(base, new EqualsSolver());
    }

}
