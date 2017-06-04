package com.turlir.abakgists.model;


import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

public class ListGistToModelMapper implements Func1<List<Gist>, List<GistModel>> {

    private final GistToModelMapper mapper;

    public ListGistToModelMapper() {
        mapper = new GistToModelMapper();
    }

    @Override
    public List<GistModel> call(List<Gist> gists) {
        final int s = gists.size();
        List<GistModel> arr = new ArrayList<>(s);
        for (int i = 0; i < s; i++) {
            Gist item = gists.get(i);
            GistModel model = mapper.call(item);
            arr.add(i, model);
        }
        return arr;
    }

}
