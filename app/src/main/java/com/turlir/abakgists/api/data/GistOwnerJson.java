package com.turlir.abakgists.api.data;


import com.google.gson.annotations.SerializedName;

public class GistOwnerJson {

    @SerializedName("login")
    public String login;

    @SerializedName("avatar_url")
    public String avatarUrl;

    @SuppressWarnings("unused")
    public GistOwnerJson() {
        // for gson, don`t use
    }

    public GistOwnerJson(String login, String avatarUrl) {
        this.login = login;
        this.avatarUrl = avatarUrl;
    }

}
