package com.turlir.abakgists.di;


import com.turlir.abakgists.AppDatabase;
import com.turlir.abakgists.allgists.AllGistsPresenter;
import com.turlir.abakgists.allgists.DataSourceFactory;
import com.turlir.abakgists.allgists.GistListInteractor;
import com.turlir.abakgists.allgists.loader.Range;
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
    public GistListInteractor provideGistListInteractor(Repository repo) {
        return new GistListInteractor(repo, new Range(0, 30, 15));
    }

    @Provides
    @Singleton
    public AllGistsPresenter provideAllGistsPresenter(DataSourceFactory factory) {
        return new AllGistsPresenter(factory);
    }

    @Provides
    @Singleton
    public DataSourceFactory provideDataSourceFactory(Repository repo) {
        return new DataSourceFactory(repo);
    }

    @Provides
    @Singleton
    public NotesPresenter provideNotesPresenter(GistListInteractor interactor) {
        return new NotesPresenter(interactor);
    }

    @Provides
    @Singleton
    public GistPresenter provideGistPresenter(AppDatabase room) {
        return new GistPresenter(new EqualsSolver(), room.gistDao());
    }
}
