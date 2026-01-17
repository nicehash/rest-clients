package com.nicehash.clients.common.gen;

import com.nicehash.clients.common.ClientCallback;
import com.nicehash.clients.common.ClientException;
import com.nicehash.clients.common.spi.*;
import com.nicehash.clients.util.options.OptionMap;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class PdfClientGenerator {

  private static final Logger log = LoggerFactory.getLogger(PdfClientGenerator.class);
  private static final Map<Class<?>, GenContext> contextMap =
      new ConcurrentHashMap<>(); // TODO ref map

  private static <S> ServiceBuilderConfiguration getServiceBuilderConfiguration(
      Class<S> serviceClass) {
    ServiceBuilderConfiguration configuration =
        serviceClass.getAnnotation(ServiceBuilderConfiguration.class);
    if (configuration == null) {
      throw new IllegalArgumentException(
          String.format("%s is missing @ServiceBuilderConfiguration!", serviceClass));
    }
    return configuration;
  }

  private static <S, T> T getResult(
      Class<S> serviceClass, Response<T> response, ClientCallback<T> callback) {
    if (response.isSuccessful()) {
      return response.body();
    } else {
      logError(response);

      ServiceApiErrorParser parser = contextMap.get(serviceClass).getParser();
      ServiceApiError apiError = parser.parse(response);
      RuntimeException exception = apiError.toException();
      if (callback != null) {
        callback.onFailure(exception);
        return null;
      } else {
        throw exception;
      }
    }
  }

  private static <T> void logError(Response<T> response) {
    try {
      Buffer buffer =
          response != null
                  && response.errorBody() != null
                  && response.errorBody().source() != null
                  && response.errorBody().source().buffer() != null
              ? response.errorBody().source().buffer().clone()
              : null;
      String errorBody = buffer != null ? buffer.readString(StandardCharsets.UTF_8) : "null";
      String responseMessage = response.message() != null ? response.message() : "null";

      log.error("logError:: Client exception: {}, message: {}", errorBody, responseMessage);
    } catch (Exception e) {
      log.error("logError:: Error body cannot be read. Exception: {}", e.getMessage());
    }
  }

  /**
   * Shutdown client -- in case we're really strict with resources.
   *
   * @param serviceClass the client's service class
   */
  public static void close(Class<?> serviceClass) {
    GenContext context = contextMap.remove(serviceClass);
    if (context != null) {
      // TODO - cleanup?
    }
  }

  public static <S> S createService(Class<S> serviceClass, OptionMap options, Logger superLog)
      throws Exception {
    ServiceBuilderConfiguration configuration = getServiceBuilderConfiguration(serviceClass);
    HttpLoggingInterceptor logging =
        new HttpLoggingInterceptor(
            message -> {
              superLog.debug(serviceClass.getSimpleName() + " retrofit: {}", message);
            });
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

    ServiceBuilder serviceBuilder = configuration.builder().newInstance();
    okhttp3.Call.Factory factory = serviceBuilder.buildCallFactory(options);
    ServiceApiErrorParser parser = serviceBuilder.parser(options);

    GenContext context = new GenContext(factory, parser);
    contextMap.put(serviceClass, context);

    PropertyReplacer replacer = options.get(Options.REPLACER, SimplePropertyReplacer.INSTANCE);
    String baseUrl = options.get(Options.BASE_URL, configuration.url());

    Retrofit.Builder builder =
        new Retrofit.Builder()
            .baseUrl(replacer.replace(baseUrl))
            .addConverterFactory(new NullOnEmptyConverterFactory())
            .addConverterFactory(new PdfConverterFactory())
            .addConverterFactory(JacksonConverterFactory.create())
            .client(client)
            .callFactory(factory);

    Executor executor = options.get(Options.EXECUTOR);
    if (executor != null) {
      builder.callbackExecutor(executor);
    }

    Retrofit retrofit = builder.build();
    return retrofit.create(serviceClass);
  }

  /** Execute a REST call and block until the response is received. */
  public static <S, T> T executeSync(Class<S> serviceClass, Call<T> call) {
    try {
      Response<T> response = call.execute();
      return getResult(serviceClass, response, null);
    } catch (IOException e) {
      throw new ClientException("Failed to execute request.", e);
    }
  }

  /** Execute a REST call in async way. */
  public static <S, T> void executeAsync(
      Class<S> serviceClass, Call<T> call, ClientCallback<T> callback) {
    call.enqueue(
        new Callback<T>() {
          @Override
          public void onResponse(Call<T> call, Response<T> response) {
            T result = getResult(serviceClass, response, callback);
            if (result != null) {
              callback.onResponse(result);
            }
          }

          @Override
          public void onFailure(Call<T> call, Throwable t) {
            callback.onFailure(t);
          }
        });
  }
}
