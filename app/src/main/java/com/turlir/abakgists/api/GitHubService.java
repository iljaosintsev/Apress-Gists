package com.turlir.abakgists.api;


import com.turlir.abakgists.api.data.GistJson;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface GitHubService {

    @GET("gists/public")
    Single<List<GistJson>> publicGist(
            @Query(value = "page") int page,
            @Query(value = "per_page") int perPage
    );

}
