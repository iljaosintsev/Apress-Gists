package com.turlir.abakgists.di;


import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.turlir.abakgists.AllGistsPresenter;
import com.turlir.abakgists.network.ApiClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppModule.class)
public class PresenterModule {

    @Provides
    @Singleton
    AllGistsPresenter provideAllGistsPresenter(ApiClient client, StorIOSQLite database) {
        return new AllGistsPresenter(client, database);
    }

}
