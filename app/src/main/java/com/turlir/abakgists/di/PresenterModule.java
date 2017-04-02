package com.turlir.abakgists.di;


import com.turlir.abakgists.AllGistsPresenter;
import com.turlir.abakgists.network.ApiClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppModule.class)
public class PresenterModule {

    @Provides
    @Singleton
    AllGistsPresenter provideAllGistsPresenter(ApiClient client) {
        return new AllGistsPresenter(client);
    }

}
