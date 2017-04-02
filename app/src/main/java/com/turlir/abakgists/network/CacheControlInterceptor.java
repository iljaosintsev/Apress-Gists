package com.turlir.abakgists.network;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class CacheControlInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .header("Cache-Control", "no-cache")
                .build();
    }

}
