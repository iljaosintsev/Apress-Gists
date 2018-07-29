package com.turlir.abakgists.api.data;

import com.turlir.abakgists.model.GistModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;

public class ListGistMapper {

    public static class Json implements Function<List<GistJson>, List<GistLocal>> {

        private final GistMapper.Json mapper;

        public Json(GistMapper.Json mapper) {
            this.mapper = mapper;
        }

        @Override
        public List<GistLocal> apply(List<GistJson> gistJson) {
            List<GistLocal> res = new ArrayList<>(gistJson.size());
            for (GistJson item : gistJson) {
                res.add(mapper.apply(item));
            }
            return res;
        }
    }

    public static class Local implements Function<List<GistLocal>, List<GistModel>> {

        private final GistMapper.Local mapper;

        public Local(GistMapper.Local mapper) {
            this.mapper = mapper;
        }

        @Override
        public List<GistModel> apply(List<GistLocal> gistJsons) {
            List<GistModel> res = new ArrayList<>(gistJsons.size());
            for (GistLocal item : gistJsons) {
                res.add(mapper.apply(item));
            }
            return res;
        }

        public void setLocal(boolean local) {
            mapper.setLocal(local);
        }

    }



}
