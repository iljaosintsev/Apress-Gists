package com.turlir.abakgists.api.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.turlir.abakgists.model.GistModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

        // not visible
        public boolean isLocal;

        private final SimpleDateFormat SERVER_FORMAT, USER_FORMAT;

        public Local() {
            SERVER_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault());
                    USER_FORMAT = new SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault());
        }

        public Local(Locale locale) {
            SERVER_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", locale);
                    USER_FORMAT = new SimpleDateFormat("d MMMM yyyy, hh:mm", locale);
        }

        @Override
        public GistModel apply(GistLocal item) {
            return new GistModel(
                    item.id,
                    item.url,
                    humanDate(item.created),
                    item.description,
                    item.ownerLogin,
                    item.ownerAvatarUrl,
                    item.note,
                    isLocal
            );
        }

        private String humanDate(String item) {
            try {
                Date date = SERVER_FORMAT.parse(item);
                return USER_FORMAT.format(date);
            } catch (ParseException e) {
                return item;
            }
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
