package com.turlir.abakgists.api.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.turlir.abakgists.model.GistsTable;

@Entity(tableName = GistsTable.BASE_NAME)
public class GistLocal {

    @ColumnInfo(name = GistsTable.ID)
    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = GistsTable.URL)
    @NonNull
    public String url;

    @ColumnInfo(name = GistsTable.CREATED)
    @NonNull
    public String created;

    @ColumnInfo(name = GistsTable.DESC)
    @NonNull
    public String description;

    @ColumnInfo(name = GistsTable.OWNER_LOGIN)
    @Nullable
    public String ownerLogin;

    @ColumnInfo(name = GistsTable.OWNER_AVATAR)
    @Nullable
    public String ownerAvatarUrl;

    @ColumnInfo(name = GistsTable.NOTE)
    @NonNull
    public String note;


    public GistLocal() {
        // for storio, don`t use
    }

    @Ignore
    public GistLocal(@NonNull String id, @NonNull String url, @NonNull String created,
                     @NonNull String description) {
        this(id, url, created, description, "", null);
    }

    @Ignore
    public GistLocal(@NonNull String id, @NonNull String url, @NonNull String created,
                     @NonNull String description, @Nullable String ownerLogin, @Nullable String ownerAvatarUrl) {
        this(id, url, created, description, "", ownerLogin, ownerAvatarUrl);
    }

    @Ignore
    public GistLocal(@NonNull String id, @NonNull String url, @NonNull String created,
                     @NonNull String description, @NonNull String note, @Nullable String ownerLogin,
                     @Nullable String ownerAvatarUrl) {
        this.id = id;
        this.url = url;
        this.created = created;
        this.description = description;
        this.note = note;
        this.ownerLogin = ownerLogin;
        this.ownerAvatarUrl = ownerAvatarUrl;
    }

    @VisibleForTesting
    @Ignore
    public GistLocal(GistLocal other) {
        this(other.id, other.url, other.created, other.description);
        note = other.note;
        if (other.ownerLogin != null) {
            ownerLogin = other.ownerLogin;
        }
        if (other.ownerAvatarUrl != null) {
            ownerAvatarUrl = other.ownerAvatarUrl;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GistLocal gistLocal = (GistLocal) o;

        if (!id.equals(gistLocal.id)) return false;
        if (!url.equals(gistLocal.url)) return false;
        if (!created.equals(gistLocal.created)) return false;
        if (!description.equals(gistLocal.description)) return false;

        if (ownerLogin != null) {
            if (!ownerLogin.equals(gistLocal.ownerLogin)) return false;
        } else {
            if (gistLocal.ownerLogin != null) return false;
        }

        if (ownerAvatarUrl != null) {
            if (!ownerAvatarUrl.equals(gistLocal.ownerAvatarUrl)) return false;
        } else {
            if (gistLocal.ownerAvatarUrl != null) return false;
        }

        return note.equals(gistLocal.note);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 229 * result + url.hashCode();
        result = 229 * result + created.hashCode();
        result = 229 * result + description.hashCode();
        result = 229 * result + (ownerLogin != null ? ownerLogin.hashCode() : 0);
        result = 229 * result + (ownerAvatarUrl != null ? ownerAvatarUrl.hashCode() : 0);
        result = 229 * result + note.hashCode();
        return result;
    }
}
