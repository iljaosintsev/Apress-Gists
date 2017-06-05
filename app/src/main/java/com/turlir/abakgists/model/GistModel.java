package com.turlir.abakgists.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = GistsTable.GISTS)
public class GistModel implements Parcelable, Cloneable {

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

    public static final Creator<GistModel> CREATOR = new Creator<GistModel>() {
        @Override
        public GistModel createFromParcel(Parcel in) {
            return new GistModel(in);
        }

        @Override
        public GistModel[] newArray(int size) {
            return new GistModel[size];
        }
    };

    public GistModel() {
        // for storio, don`t use
    }

    public GistModel(@NonNull String id, @NonNull String url, @NonNull String created,
                     @Nullable String description) {
        this(id, url, created, description, null, null);
    }

    public GistModel(@NonNull String id, @NonNull String url, @NonNull String created,
                     @Nullable String description, @Nullable String ownerLogin, @Nullable String ownerAvatarUrl) {
        this(id, url, created, description, null, ownerLogin, ownerAvatarUrl);
    }

    public GistModel(@NonNull String id, @NonNull String url, @NonNull String created,
                     @Nullable String description, @Nullable String note, @Nullable String ownerLogin,
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
    public GistModel(GistModel other) {
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

    private GistModel(Parcel in) {
        url = in.readString();
        id = in.readString();
        description = in.readString();
        created = in.readString();
        ownerLogin = in.readString();
        ownerAvatarUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(created);
        dest.writeString(ownerAvatarUrl);
        dest.writeString(created);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GistModel gistModel = (GistModel) o;

        if (!id.equals(gistModel.id)) return false;
        if (!url.equals(gistModel.url)) return false;
        if (!created.equals(gistModel.created)) return false;

        if (description != null) {
            if (!description.equals(gistModel.description)) return false;
        } else {
            if (gistModel.description != null) return false;
        }

        if (ownerLogin != null) {
            if (!ownerLogin.equals(gistModel.ownerLogin)) return false;
        } else {
            if (gistModel.ownerLogin != null) return false;
        }

        if (ownerAvatarUrl != null) {
            if (!ownerAvatarUrl.equals(gistModel.ownerAvatarUrl)) return false;
        } else {
            if (gistModel.ownerAvatarUrl != null) return false;
        }

        if (note != null) {
            return note.equals(gistModel.note);
        }
        else {
            return gistModel.note == null;
        }
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 113 * result + url.hashCode();
        result = 113 * result + created.hashCode();
        result = 113 * result + (description != null ? description.hashCode() : 0);
        result = 113 * result + (ownerLogin != null ? ownerLogin.hashCode() : 0);
        result = 113 * result + (ownerAvatarUrl != null ? ownerAvatarUrl.hashCode() : 0);
        result = 113 * result + (note != null ? note.hashCode() : 0);
        return result;
    }
}