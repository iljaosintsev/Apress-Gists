package com.turlir.abakgists.di;


import android.support.annotation.VisibleForTesting;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.turlir.abakgists.allgists.AllGistsPresenter;
import com.turlir.abakgists.allgists.view.AllGistsFragment;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.gist.GistActivity;
import com.turlir.abakgists.notes.view.NotesFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, DatabaseModule.class, PresenterModule.class} )
@Singleton
public interface AppComponent {

    void inject(AllGistsFragment fragment);

    void inject(GistActivity fragment);

    void inject(NotesFragment fragment);

    @VisibleForTesting
    Repository provideRepository();

    @VisibleForTesting
    StorIOSQLite provideStorIOSQLite();

    @VisibleForTesting
    AllGistsPresenter provideAllGistsPresenter();

}
