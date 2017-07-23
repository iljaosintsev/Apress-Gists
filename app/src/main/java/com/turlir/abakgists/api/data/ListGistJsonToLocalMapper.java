package com.turlir.abakgists.api.data;


import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

public class ListGistJsonToLocalMapper implements Func1<List<GistJson>, List<GistLocal>> {

    private final GistJsonToLocalMapper mapper;

    public ListGistJsonToLocalMapper() {
        mapper = new GistJsonToLocalMapper();
    }

    @Override
    public List<GistLocal> call(List<GistJson> gists) {
        final int s = gists.size();
        List<GistLocal> arr = new ArrayList<>(s);
        for (int i = 0; i < s; i++) {
            GistJson item = gists.get(i);
            GistLocal model = mapper.call(item);
            arr.add(i, model);
        }
        return arr;
    }

}
