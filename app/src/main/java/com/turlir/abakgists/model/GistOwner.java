package com.turlir.abakgists.model;


import com.google.gson.annotations.SerializedName;

public class GistOwner {

    @SerializedName("login")
    public String login;

    @SerializedName("avatar_url")
    public String avatarUrl;

    public GistOwner() {

    }

    public GistOwner(String login, String avatarUrl) {
        this.login = login;
        this.avatarUrl = avatarUrl;
    }

}
