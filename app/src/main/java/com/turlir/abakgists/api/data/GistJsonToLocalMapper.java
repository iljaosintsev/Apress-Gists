package com.turlir.abakgists.api.data;


import rx.functions.Func1;

class GistJsonToLocalMapper implements Func1<GistJson, GistLocal> {

    @Override
    public GistLocal call(GistJson item) {
        if (item.owner != null) {
            return new GistLocal(item.id, item.url, item.created, item.description,
                    item.owner.login, item.owner.avatarUrl);
        } else {
            return new GistLocal(item.id, item.url, item.created, item.description);
        }
    }

}
