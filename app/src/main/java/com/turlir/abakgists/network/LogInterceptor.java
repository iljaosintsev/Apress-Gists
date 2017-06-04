package com.turlir.abakgists.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import timber.log.Timber;

public class LogInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        log("Request: " + request.url().toString());
        if (request.method().equals("POST")) {
            log("Request body: " + body2str(request));
        }

        Response response = chain.proceed(request);
        String bodyString = response.body().string();
        log("Response (" + response.code() + "): " + bodyString);

        MediaType mediaType = response.body().contentType();
        ResponseBody body = ResponseBody.create(mediaType, bodyString);
        return response.newBuilder().body(body).build();
    }

    private String body2str(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            if ((copy.body().contentLength() > 1024 * 1024)) { // > 1 MB
                return "<too long>";
            } else {
                copy.body().writeTo(buffer);
                return buffer.readUtf8();
            }
        } catch (IOException e) {
            return "<no body>";
        }
    }

    private static void log(String message) {
        Timber.i(message);
    }

}
