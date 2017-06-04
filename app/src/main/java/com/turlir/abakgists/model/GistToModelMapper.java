package com.turlir.abakgists.model;


import rx.functions.Func1;

class GistToModelMapper implements Func1<Gist, GistModel> {

    @Override
    public GistModel call(Gist item) {
        if (item.owner != null) {
            return new GistModel(item.id, item.url, item.created, item.description,
                    item.owner.login, item.owner.avatarUrl);
        } else {
            return new GistModel(item.id, item.url, item.created, item.description);
        }
    }

}
