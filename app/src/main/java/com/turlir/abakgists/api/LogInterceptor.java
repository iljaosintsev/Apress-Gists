package com.turlir.abakgists.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import timber.log.Timber;

public class LogInterceptor implements Interceptor {

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
        } else {
            Timber.i("Response (%d): %s", response.code(), bodyString);
        }

        MediaType mediaType = response.body().contentType();
        ResponseBody body = ResponseBody.create(mediaType, bodyString);
        return response.newBuilder().body(body).build();
    }

}
