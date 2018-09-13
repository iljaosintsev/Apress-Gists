package com.turlir.abakgists.di;


import com.turlir.abakgists.AppDatabase;
import com.turlir.abakgists.allgists.NotesInteractor;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.gist.GistDeleteBus;
import com.turlir.abakgists.gist.GistInteractor;
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

    /*@Provides
    public SourceListing provideGistListInteractor(Repository repo) {
        return new SourceListing(repo);
    }*/

    @Provides
    @Singleton
    public NotesPresenter provideNotesPresenter(NotesInteractor interactor) {
        return new NotesPresenter(interactor);
    }

    @Provides
    public GistInteractor provideGistInteractor(AppDatabase room) {
        return new GistInteractor(room.gistDao());
    }

    @Provides
    @Singleton
    public GistDeleteBus provideGistDeleteBus() {
        return new GistDeleteBus();
    }
}
