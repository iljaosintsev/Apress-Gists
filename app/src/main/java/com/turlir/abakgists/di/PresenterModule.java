package com.turlir.abakgists.di;


import com.turlir.abakgists.AppDatabase;
import com.turlir.abakgists.allgists.DataSourceFactory;
import com.turlir.abakgists.allgists.NotesInteractor;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.gist.EqualsSolver;
import com.turlir.abakgists.gist.GistPresenter;
import com.turlir.abakgists.notes.NotesPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppModule.class)
public class PresenterModule {

    @Provides
    public NotesInteractor provideNotesInteractor(Repository repo) {
        return new NotesInteractor(repo);
    }

    @Provides
    @Singleton
    public DataSourceFactory provideDataSourceFactory(Repository repo) {
        return new DataSourceFactory(repo);
    }

    @Provides
    @Singleton
    public NotesPresenter provideNotesPresenter(NotesInteractor interactor) {
        return new NotesPresenter(interactor);
    }

    @Provides
    @Singleton
    public GistPresenter provideGistPresenter(AppDatabase room) {
        return new GistPresenter(new EqualsSolver(), room.gistDao());
    }
}
