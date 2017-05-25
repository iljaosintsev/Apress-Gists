package com.turlir.abakgists.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = GistsTable.GISTS)
public class Gist implements Parcelable {

    @SerializedName("url")
    @StorIOSQLiteColumn(name = GistsTable.URL)
    @NonNull
    public String url;

    @SerializedName("id")
    @StorIOSQLiteColumn(name = GistsTable.ID, key = true)
    @NonNull
    public String id;

    @SerializedName("description")
    @StorIOSQLiteColumn(name = GistsTable.DESC)
    @Nullable
    public String description;

    @SerializedName("created_at")
    @StorIOSQLiteColumn(name = GistsTable.CREATED)
    @NonNull
    public String created;

    ////////

    @SerializedName("owner")
    @Nullable
    public GistOwner owner;

    @StorIOSQLiteColumn(name = GistsTable.OWNER_LOGIN)
    @Nullable
    public String ownerLogin;

    @StorIOSQLiteColumn(name = GistsTable.OWNER_AVATAR)
    @Nullable
    public String ownerAvatarUrl;

    ////////

    @StorIOSQLiteColumn(name = GistsTable.NOTE)
    @Nullable
    public String note;

    public static final Creator<Gist> CREATOR = new Creator<Gist>() {
        @Override
        public Gist createFromParcel(Parcel in) {
            return new Gist(in);
        }

        @Override
        public Gist[] newArray(int size) {
            return new Gist[size];
        }
    };

    public Gist() {

    }

    @VisibleForTesting
    public Gist(@NonNull String url, @NonNull String id, @NonNull String created) {
        this.url = url;
        this.id = id;
        this.created = created;
    }

    @VisibleForTesting
    public Gist(@NonNull String id, @NonNull String url, @NonNull String created,
                @NonNull String desc, @NonNull String note, @NonNull String ownerAvatarUrl,
                @NonNull String ownerLogin) {
        this.id = id;
        this.url = url;
        this.created = created;
        this.description = desc;
        this.note = note;
        this.ownerAvatarUrl = ownerAvatarUrl;
        this.ownerLogin = ownerLogin;
    }

    protected Gist(Parcel in) {
        url = in.readString();
        id = in.readString();
        description = in.readString();
        created = in.readString();
        ownerLogin = in.readString();
        ownerAvatarUrl = in.readString();
        note = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(created);
        dest.writeString(ownerLogin);
        dest.writeString(ownerAvatarUrl);
        dest.writeString(note);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gist gist = (Gist) o;

        if (!url.equals(gist.url)) return false;
        if (!id.equals(gist.id)) return false;
        if (!created.equals(gist.created)) return false;

        if (description != null) {
            if (!description.equals(gist.description)) return false;
        } else {
            if (gist.description != null) return false;
        }

        if (ownerLogin != null) {
            if (!ownerLogin.equals(gist.ownerLogin)) return false;
        } else {
            if (gist.ownerLogin != null) return false;
        }

        if (ownerAvatarUrl != null) {
            if (!ownerAvatarUrl.equals(gist.ownerAvatarUrl)) return false;
        } else {
            if (gist.ownerAvatarUrl != null) return false;
        }

        if (note != null) {
            return note.equals(gist.note);
        }
        else {
            return gist.note == null;
        }
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + created.hashCode();
        result = 31 * result + (ownerLogin != null ? ownerLogin.hashCode() : 0);
        result = 31 * result + (ownerAvatarUrl != null ? ownerAvatarUrl.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        return result;
    }
}
