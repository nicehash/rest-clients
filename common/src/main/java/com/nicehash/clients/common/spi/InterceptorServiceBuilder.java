package com.nicehash.clients.common.spi;

import com.nicehash.clients.common.util.AuthenticationInterceptor;
import com.nicehash.clients.util.options.OptionMap;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

public abstract class InterceptorServiceBuilder extends AbstractServiceBuilder {
  @Override
  protected void buildHttpClient(OptionMap options, OkHttpClient.Builder builder) {
    Interceptor interceptor = options.get(Options.AUTH_INTERCEPTOR);
    if (interceptor == null) {
      interceptor = createInterceptor(options);
    }
    builder.addInterceptor(interceptor);
  }

  protected Interceptor createInterceptor(OptionMap options) {
    String key = options.get(Options.KEY);
    String secret = options.get(Options.SECRET);
    return new AuthenticationInterceptor(key, secret);
  }
}
