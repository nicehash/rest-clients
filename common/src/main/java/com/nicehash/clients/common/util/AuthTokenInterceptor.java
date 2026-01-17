package com.nicehash.clients.common.util;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A request interceptor that injects the API Key Header into requests, and signs messages, whenever
 * required.
 */
public class AuthTokenInterceptor implements Interceptor {
  private final String token;

  public AuthTokenInterceptor(String token) {
    this.token = token;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request newRequest =
        chain
            .request()
            .newBuilder()
            .addHeader("Authorization", String.format("Bearer %s", token))
            .build();
    return chain.proceed(newRequest);
  }
}
