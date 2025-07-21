package com.nicehash.clients.common.gen;

import com.nicehash.clients.common.ClientCallback;
import com.nicehash.clients.common.ClientException;
import com.nicehash.clients.common.spi.*;
import com.nicehash.clients.util.options.OptionMap;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.concurrent.Executor;


public class ExternalClientGenerator {
    private final static Logger log = LoggerFactory.getLogger(ExternalClientGenerator.class);

    public static boolean isJackson = true;

    private static <S> ServiceBuilderConfiguration getServiceBuilderConfiguration(Class<S> serviceClass) {
        ServiceBuilderConfiguration configuration = serviceClass.getAnnotation(ServiceBuilderConfiguration.class);
        if (configuration == null) {
            throw new IllegalArgumentException(String.format("%s is missing @ServiceBuilderConfiguration!", serviceClass));
        }
        return configuration;
    }

    private static <S, T> T getResult(Class<S> serviceClass, Response<T> response, ClientCallback<T> callback) {
        if (response.isSuccessful()) {
            return response.body();
        } else {
            RuntimeException exception;

            try {
                exception = new RuntimeException(response.errorBody().string());
            } catch (IOException e) {
                log.error("Parsing error failed. Response: {} | Exception: {}", response, e.getMessage());
                exception = new RuntimeException(response.toString());
            }
            if (callback != null) {
                callback.onFailure(exception);
                return null;
            } else {
                throw exception;
            }
        }
    }

    /**
     * Shutdown client -- in case we're really strict with resources.
     *
     * @param serviceClass the client's service class
     */
    public static void close(Class<?> serviceClass) {
    }

    public static <S> S createService(Class<S> serviceClass, OptionMap options) throws Exception {
        ServiceBuilderConfiguration configuration = getServiceBuilderConfiguration(serviceClass);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> {
            log.debug(serviceClass.getSimpleName() + " retrofit: ", message);
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        ServiceBuilder serviceBuilder = configuration.builder().newInstance();
        okhttp3.Call.Factory factory = serviceBuilder.buildCallFactory(options);
        ServiceApiErrorParser parser = serviceBuilder.parser(options);

        GenContext context = new GenContext(factory, parser);

        PropertyReplacer replacer = options.get(Options.REPLACER, SimplePropertyReplacer.INSTANCE);
        String baseUrl = options.get(Options.BASE_URL, configuration.url());

        Retrofit.Builder builder;
        if(isJackson) {
            builder = new Retrofit.Builder()
                    .baseUrl(replacer.replace(baseUrl))
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .client(client)
                    .callFactory(factory);
        }else {
            builder = new Retrofit.Builder()
                    .baseUrl(replacer.replace(baseUrl))
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(new ByteArrayConverterFactory())
                    .client(client)
                    .callFactory(factory);
        }

        Executor executor = options.get(Options.EXECUTOR);
        if (executor != null) {
            builder.callbackExecutor(executor);
        }

        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }

    /**
     * Execute a REST call and block until the response is received.
     */
    public static <S, T> T executeSync(Class<S> serviceClass, Call<T> call) {
        try {
            Response<T> response = call.execute();
            return getResult(serviceClass, response, null);
        } catch (IOException e) {
            throw new ClientException("Failed to execute request.", e);
        }
    }

    /**
     * Execute a REST call in async way.
     */
    public static <S, T> void executeAsync(Class<S> serviceClass, Call<T> call, ClientCallback<T> callback) {
        call.enqueue(new Callback<T>() {
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
