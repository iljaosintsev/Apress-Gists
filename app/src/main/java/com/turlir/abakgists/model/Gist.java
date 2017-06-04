package com.turlir.abakgists.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.SerializedName;

public class Gist {

    @SerializedName("id")
    @NonNull
    public String id;

    @SerializedName("url")
    @NonNull
    public String url;

    @SerializedName("created_at")
    @NonNull
    public String created;

    @SerializedName("description")
    @Nullable
    public String description;

    @SerializedName("owner")
    @Nullable
    public GistOwner owner;

    public Gist() {
        // for gson, don`t use
    }

    @VisibleForTesting
    public Gist(Gist other) {
        this(other.id, other.url, other.created, other.description);
    }

    @VisibleForTesting
    public Gist(@NonNull String id, @NonNull String url, @NonNull String created, @Nullable String desc) {
        this.id = id;
        this.url = url;
        this.created = created;
        this.description = desc;
    }

    @VisibleForTesting
    public Gist(@NonNull String id, @NonNull String url, @NonNull String created, @Nullable String desc,
                @NonNull GistOwner owner) {
        this(id, url, created,desc);
        this.owner = owner;
    }

}
