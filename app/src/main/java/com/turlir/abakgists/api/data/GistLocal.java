package com.turlir.abakgists.api.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;
import com.turlir.abakgists.model.GistsTable;

@StorIOSQLiteType(table = GistsTable.GISTS)
public class GistLocal {

    @StorIOSQLiteColumn(name = GistsTable.ID, key = true)
    @NonNull
    public String id;

    @StorIOSQLiteColumn(name = GistsTable.URL)
    @NonNull
    public String url;

    @StorIOSQLiteColumn(name = GistsTable.CREATED)
    @NonNull
    public String created;

    @StorIOSQLiteColumn(name = GistsTable.DESC)
    @Nullable
    public String description;

    @StorIOSQLiteColumn(name = GistsTable.OWNER_LOGIN)
    @Nullable
    public String ownerLogin;

    @StorIOSQLiteColumn(name = GistsTable.OWNER_AVATAR)
    @Nullable
    public String ownerAvatarUrl;

    @StorIOSQLiteColumn(name = GistsTable.NOTE)
    @Nullable
    public String note;


    public GistLocal() {
        // for storio, don`t use
    }

    public GistLocal(@NonNull String id, @NonNull String url, @NonNull String created,
                     @Nullable String description) {
        this(id, url, created, description, null, null);
    }

    public GistLocal(@NonNull String id, @NonNull String url, @NonNull String created,
                     @Nullable String description, @Nullable String ownerLogin, @Nullable String ownerAvatarUrl) {
        this(id, url, created, description, null, ownerLogin, ownerAvatarUrl);
    }

    public GistLocal(@NonNull String id, @NonNull String url, @NonNull String created,
                     @Nullable String description, @Nullable String note, @Nullable String ownerLogin,
                     @Nullable String ownerAvatarUrl) {
        this.id = id;
        this.url = url;
        this.created = created;
        this.description = description;
        this.note = note;
        overrideOwnerLogin(ownerLogin);
        this.ownerAvatarUrl = ownerAvatarUrl;
    }

    @VisibleForTesting
    public GistLocal(GistLocal other) {
        this(other.id, other.url, other.created, other.description);
        if (other.note != null) {
            note = other.note;
        }
        if (other.ownerLogin != null) {
            ownerLogin = other.ownerLogin;
        }
        if (other.ownerAvatarUrl != null) {
            ownerAvatarUrl = other.ownerAvatarUrl;
        }
    }

    private void overrideOwnerLogin(@Nullable String ownerLogin) {
        if (ownerLogin != null) {
            this.ownerLogin = ownerLogin;
        } else {
            this.ownerLogin = "anonymous";
        }
    }

}
