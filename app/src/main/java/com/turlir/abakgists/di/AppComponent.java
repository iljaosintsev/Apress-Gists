package com.turlir.abakgists.di;


import com.turlir.abakgists.all.AllInOnePresenter;
import com.turlir.abakgists.allgists.AllGistsPresenter;
import com.turlir.abakgists.allgists.view.AllGistsFragment;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.gist.GistActivity;
import com.turlir.abakgists.gist.GistPresenter;
import com.turlir.abakgists.notes.view.NotesFragment;

import javax.inject.Singleton;

import androidx.annotation.VisibleForTesting;
import dagger.Component;

@Component(modules = {AppModule.class, DatabaseModule.class, PresenterModule.class} )
@Singleton
public interface AppComponent {

    void inject(AllGistsFragment fragment);

    void inject(GistActivity fragment);

    void inject(NotesFragment fragment);

    void inject(AllGistsPresenter presenter);

    void inject(GistPresenter presenter);

    void inject(AllInOnePresenter presenter);

    @VisibleForTesting
    Repository provideRepository();
}
