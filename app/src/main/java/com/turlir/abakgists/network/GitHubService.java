package com.turlir.abakgists.network;


import com.turlir.abakgists.model.Gist;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface GitHubService {

    @GET("gists/public?per_page=30")
    Observable<List<Gist>> publicGist(
            @Query(value = "page") int page
    );

}
