package com.turlir.abakgists.model;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = "gists")
public class Gist {

    @StorIOSQLiteColumn(name = "_id", key = true)
    @Nullable
    public Long number;

    @SerializedName("url")
    @StorIOSQLiteColumn(name = "url")
    @NonNull
    public String url;

    @SerializedName("id")
    @StorIOSQLiteColumn(name = "id")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gist gist = (Gist) o;

        if (number != null ? !number.equals(gist.number) : gist.number != null) return false;
        if (!url.equals(gist.url)) return false;
        if (!id.equals(gist.id)) return false;
        if (description != null ? !description.equals(gist.description) : gist.description != null)
            return false;
        return created.equals(gist.created);

    }

    @Override
    public int hashCode() {
        int result = number != null ? number.hashCode() : 0;
        result = 17 * result + url.hashCode();
        result = 17 * result + id.hashCode();
        result = 17 * result + (description != null ? description.hashCode() : 0);
        result = 17 * result + created.hashCode();
        return result;
    }
}
