package com.turlir.abakgists.di;


import com.turlir.abakgists.allgists.AllGistsFragment;
import com.turlir.abakgists.GistActivity;
import com.turlir.abakgists.notes.NotesFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, DatabaseModule.class, PresenterModule.class} )
@Singleton
public interface AppComponent {

    void inject(AllGistsFragment fragment);

    void inject(GistActivity fragment);

    void inject(NotesFragment fragment);
}
