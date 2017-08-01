package com.turlir.abakgists.api.data;

import com.turlir.abakgists.model.GistModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

public class ListGistMapper {

    public static class Json implements Func1<List<GistJson>, List<GistLocal>> {

        private final GistMapper.Json mapper;

        public Json(GistMapper.Json mapper) {
            this.mapper = mapper;
        }

        @Override
        public List<GistLocal> call(List<GistJson> gistJsons) {
            List<GistLocal> res = new ArrayList<>(gistJsons.size());
            for (GistJson item : gistJsons) {
                res.add(mapper.call(item));
            }
            return res;
        }
    }

    public static class Local implements Func1<List<GistLocal>, List<GistModel>> {

        private final GistMapper.Local mapper;

        public Local(GistMapper.Local mapper) {
            this.mapper = mapper;
        }

        @Override
        public List<GistModel> call(List<GistLocal> gistLocals) {
            List<GistModel> res = new ArrayList<>(gistLocals.size());
            for (GistLocal item : gistLocals) {
                res.add(mapper.call(item));
            }
            return res;
        }

        public void setLocal(boolean local) {
            mapper.setLocal(local);
        }

    }


}
