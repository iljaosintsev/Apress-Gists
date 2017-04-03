package com.turlir.abakgists.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = "gists")
public class Gist implements Parcelable {

    @SerializedName("url")
    @StorIOSQLiteColumn(name = "url")
    @NonNull
    public String url;

    @SerializedName("id")
    @StorIOSQLiteColumn(name = "id", key = true)
    @NonNull
    public String id;

    @SerializedName("description")
    @StorIOSQLiteColumn(name = "desc")
    @Nullable
    public String description;

    @SerializedName("created_at")
    @StorIOSQLiteColumn(name = "created")
    @NonNull
    public String created;

    ////////

    @SerializedName("owner")
    @Nullable
    public GistOwner owner;

    @StorIOSQLiteColumn(name = "ownerLogin")
    @Nullable
    public String ownerLogin;

    @StorIOSQLiteColumn(name = "ownerAvatarUrl")
    @Nullable
    public String ownerAvatarUrl;

    ////////

    @StorIOSQLiteColumn(name = "note")
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

    protected Gist(Parcel in) {
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
        dest.writeString(ownerLogin);
        dest.writeString(ownerAvatarUrl);
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
        if (description != null ? !description.equals(gist.description) : gist.description != null)
            return false;
        return created.equals(gist.created);

    }

    @Override
    public int hashCode() {
        int result = 17 + url.hashCode();
        result = 17 * result + id.hashCode();
        result = 17 * result + (description != null ? description.hashCode() : 0);
        result = 17 * result + created.hashCode();
        return result;
    }

}
