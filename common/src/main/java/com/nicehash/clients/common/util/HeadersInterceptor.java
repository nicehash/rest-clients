package com.nicehash.clients.common.util;

import com.nicehash.clients.common.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Pass any Headers's headers to external service.
 * Headers get cleared after being set on request.
 */
public class HeadersInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Map<String, List<String>> headers = Headers.getHeaders();
        try {
            if (headers.isEmpty() == false) {
                Request.Builder builder = request.newBuilder();

                for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    List<String> values = entry.getValue();
                    if (values.size() == 1) {
                        builder.header(key, values.get(0));
                    } else {
                        values.forEach(v -> builder.addHeader(key, v));
                    }
                }

                request = builder.build();
            }
        } finally {
            Headers.clear();
        }

        return chain.proceed(request);
    }
}