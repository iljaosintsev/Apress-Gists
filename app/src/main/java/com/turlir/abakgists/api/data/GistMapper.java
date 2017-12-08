package com.turlir.abakgists.api.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.turlir.abakgists.model.GistModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import rx.functions.Func1;

public class GistMapper {

    public static class Json implements Func1<GistJson, GistLocal> {

        @Override
        public GistLocal call(GistJson item) {
            String desc = safeAssign(item.description);
            GistOwnerJson o = item.owner;
            if (o != null) {
                return new GistLocal(item.id, item.url, item.created, desc, o.login, o.avatarUrl);
            } else {
                return new GistLocal(item.id, item.url, item.created, desc, null, null);
            }
        }

    }

    public static class Local implements Func1<GistLocal, GistModel> {

        private boolean isLocal;

        private static final SimpleDateFormat
                SERVER_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault()),
                USER_FORMAT = new SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault());

        static {
            SERVER_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        @Override
        public GistModel call(GistLocal item) {
            final String created = parseDate(item);
            return new GistModel(
                    item.id,
                    item.url,
                    created,
                    item.description,
                    item.ownerLogin,
                    item.ownerAvatarUrl,
                    item.note,
                    isLocal
            );
        }

        private String parseDate(GistLocal item) {
            try {
                Date date = SERVER_FORMAT.parse(item.created);
                return USER_FORMAT.format(date);
            } catch (ParseException e) {
                return item.created;
            }
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
