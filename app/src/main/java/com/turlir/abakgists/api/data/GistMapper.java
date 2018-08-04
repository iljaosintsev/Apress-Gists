package com.turlir.abakgists.api.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.turlir.abakgists.model.GistModel;

import io.reactivex.functions.Function;

public class GistMapper {

    public static class Json implements Function<GistJson, GistLocal> {

        @Override
        public GistLocal apply(GistJson item) {
            String desc = safeAssign(item.description);
            GistOwnerJson o = item.owner;
            if (o != null) {
                return new GistLocal(item.id, item.url, item.created, desc, o.login, o.avatarUrl);
            } else {
                return new GistLocal(item.id, item.url, item.created, desc, null, null);
            }
        }
    }

    public static class Local implements Function<GistLocal, GistModel> {

        public boolean isLocal;

        @Override
        public GistModel apply(GistLocal item) {
            return new GistModel(
                    item.id,
                    item.url,
                    item.created,
                    item.description,
                    item.ownerLogin,
                    item.ownerAvatarUrl,
                    item.note,
                    isLocal
            );
        }
    }


    @NonNull
    private static String safeAssign(@Nullable String value) {
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }
}
