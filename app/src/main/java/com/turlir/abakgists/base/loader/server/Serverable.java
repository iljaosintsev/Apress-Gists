package com.turlir.abakgists.base.loader.server;

import io.reactivex.Single;

public interface Serverable {

    Single<Integer> loadFromServer(LoadableItem page);

    Single<Integer> update(LoadableItem w);
}
