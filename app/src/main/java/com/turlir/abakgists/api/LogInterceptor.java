package com.turlir.abakgists.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import timber.log.Timber;

public class LogInterceptor implements Interceptor {

    private static final String JSON_HEADER = "application/json; charset=utf-8";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Timber.i("Request: %s", request.url().toString());
        if (request.method().equals("POST")) {
            Timber.i("Request body: %s", request);
        }

        Response response = chain.proceed(request);
        ResponseBody body = response.body();

        if (body != null) {
            String bodyString = body.string();
            if (JSON_HEADER.equals(response.headers().get("Content-Type"))) {
                Timber.i("Response (%d): %s", response.code(), bodyString);
            } else {
                Timber.i("Response (%d)", response.code());
            }
            MediaType mediaType = body.contentType();
            body = ResponseBody.create(mediaType, bodyString);
            return response.newBuilder().body(body).build();

        } else {
            return null;
        }
    }
}
