package com.nicehash.clients.common.util;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.Interceptor;
import okhttp3.RequestBody;
import okio.Buffer;

public abstract class AbstractAuthenticationInterceptor implements Interceptor {

  protected final Logger log = Logger.getLogger(getClass().getSimpleName());

  protected final String apiKey;
  protected final String secret;

  protected Level debugLevel = Level.FINEST; // the lowest, force higher, if needed

  protected AbstractAuthenticationInterceptor(String apiKey, String secret) {
    this.apiKey = apiKey;
    this.secret = secret;
  }

  /**
   * Extracts the request body into a String.
   *
   * @return request body as a string
   */
  @SuppressWarnings("unused")
  protected static String bodyToString(RequestBody request) {
    try (final Buffer buffer = new Buffer()) {
      if (request != null) {
        request.writeTo(buffer);
      } else {
        return "";
      }
      return buffer.readUtf8();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final AbstractAuthenticationInterceptor that = (AbstractAuthenticationInterceptor) o;
    return Objects.equals(apiKey, that.apiKey) && Objects.equals(secret, that.secret);
  }

  @Override
  public int hashCode() {
    return Objects.hash(apiKey, secret);
  }
}
