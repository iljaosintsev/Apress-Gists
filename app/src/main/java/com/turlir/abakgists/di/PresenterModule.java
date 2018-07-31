package com.turlir.abakgists.di;


import com.turlir.abakgists.AppDatabase;
import com.turlir.abakgists.RoomPresenter;
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
    public GistListInteractor provideGistListInteractor(Repository repo) {
        return new GistListInteractor(repo);
    }

    @Provides
    public NotesInteractor provideNotesInteractor(Repository repo) {
        return new NotesInteractor(repo);
    }

    @Provides
    @Singleton
    public AllGistsPresenter provideAllGistsPresenter(GistListInteractor interactor) {
        return new AllGistsPresenter(interactor);
    }

    @Provides
    @Singleton
    public NotesPresenter provideNotesPresenter(NotesInteractor interactor) {
        return new NotesPresenter(interactor);
    }

    @Provides
    @Singleton
    public GistPresenter provideGistPresenter() {
        return new GistPresenter(new EqualsSolver());
    }

    @Provides
    @Singleton
    public RoomPresenter provideRoomPresenter(AppDatabase room) {
        return new RoomPresenter(room);
    }

}
