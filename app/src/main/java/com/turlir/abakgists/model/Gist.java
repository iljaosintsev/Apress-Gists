package com.turlir.abakgists.model;


import com.google.gson.annotations.SerializedName;

public class Gist {

    @SerializedName("url")
    public String url;

    @SerializedName("id")
    public String id;

    @SerializedName("description")
    public String description;

    @SerializedName("created_at")
    public String created;

    @SerializedName("html_url")
    public String htmlUrl;

}
