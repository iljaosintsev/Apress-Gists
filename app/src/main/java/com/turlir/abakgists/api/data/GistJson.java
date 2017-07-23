package com.turlir.abakgists.api.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.SerializedName;

public class GistJson {

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
    public GistOwnerJson owner;

    @SuppressWarnings("unused")
    public GistJson() {
        // for gson, don`t use
    }

    @VisibleForTesting
    public GistJson(GistJson other) {
        this(other.id, other.url, other.created, other.description);
    }

    @VisibleForTesting
    public GistJson(@NonNull String id, @NonNull String url, @NonNull String created, @Nullable String desc) {
        this.id = id;
        this.url = url;
        this.created = created;
        this.description = desc;
    }

    @VisibleForTesting
    public GistJson(@NonNull String id, @NonNull String url, @NonNull String created, @Nullable String desc,
                    @NonNull GistOwnerJson owner) {
        this(id, url, created,desc);
        this.owner = owner;
    }

}
