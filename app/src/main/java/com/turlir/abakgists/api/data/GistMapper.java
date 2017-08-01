package com.turlir.abakgists.api.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.turlir.abakgists.model.GistModel;

import rx.functions.Func1;

public class GistMapper {

    public static class Json implements Func1<GistJson, GistLocal> {

        @Override
        public GistLocal call(GistJson item) {
            String desc = safeAssign(item.description);
            GistOwnerJson o;
            if ((o = item.owner) != null) {
                return new GistLocal(item.id, item.url, item.created, desc, o.login, o.avatarUrl);
            } else {
                return new GistLocal(item.id, item.url, item.created, desc, null, null);
            }
        }

    }

    public static class Local implements Func1<GistLocal, GistModel> {

        private boolean isLocal;

        @Override
        public GistModel call(GistLocal item) {
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

        public void setLocal(boolean local) {
            isLocal = local;
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
