package com.turlir.abakgists.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import timber.log.Timber;

public class LogInterceptor implements Interceptor {

    private final Gson mGson;
    private final JsonParser mParser;

    public LogInterceptor() {
        mGson = new GsonBuilder().setPrettyPrinting().create();
        mParser = new JsonParser();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Timber.i("Request: %s", request.url().toString());
        if (request.method().equals("POST")) {
            Timber.i("Request body: %s", request);
        }

        Response response = chain.proceed(request);
        String bodyString = response.body().string();
        if ("application/json; charset=utf-8".equals(response.headers().get("Content-Type"))) {
            Timber.i("Response (%d)", response.code());
            //prettyJsonLog(bodyString);
        } else {
            Timber.i("Response (%d): %s", response.code(), bodyString);
        }

        MediaType mediaType = response.body().contentType();
        ResponseBody body = ResponseBody.create(mediaType, bodyString);
        return response.newBuilder().body(body).build();
    }

    private void prettyJsonLog(String message) {
        JsonElement json = mParser.parse(message);
        String prettyJson = mGson.toJson(json);
        Timber.i(prettyJson);
    }

}
