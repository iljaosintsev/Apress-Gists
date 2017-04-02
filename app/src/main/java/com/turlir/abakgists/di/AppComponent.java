package com.turlir.abakgists.di;


import com.turlir.abakgists.AllGistsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, DatabaseModule.class, PresenterModule.class} )
@Singleton
public interface AppComponent {

    void inject(AllGistsFragment fragment);

}
