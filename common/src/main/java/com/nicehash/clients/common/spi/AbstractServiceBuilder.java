package com.nicehash.clients.common.spi;

import com.nicehash.clients.common.util.HeadersInterceptor;
import com.nicehash.clients.util.options.OptionMap;
import okhttp3.Call;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;


public abstract class AbstractServiceBuilder implements ServiceBuilder {
    @Override
    public Call.Factory buildCallFactory(OptionMap optionMap) {
        Call.Factory factory = optionMap.get(Options.CALL_FACTORY);
        if (factory == null) {
            OkHttpClient client = optionMap.get(Options.HTTP_CLIENT);
            if (client == null) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();

                builder.addInterceptor(new HeadersInterceptor());

                buildHttpClient(optionMap, builder);

                Long readTimeout = optionMap.get(Options.READ_TIMEOUT);
                if (readTimeout != null) {
                    builder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
                }

                Long writeTimeout = optionMap.get(Options.WRITE_TIMEOUT);
                if (writeTimeout != null) {
                    builder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
                }

                Long connectionTimeout = optionMap.get(Options.CONNECTION_TIMEOUT);
                if(connectionTimeout!=null){
                    builder.connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS);
                }

                client = builder.build();
            }
            return client;
        }
        return factory;
    }

    protected abstract void buildHttpClient(OptionMap optionMap, OkHttpClient.Builder builder);

    public ServiceApiErrorParser parser(OptionMap optionMap) {
        return optionMap.get(Options.ERROR_PARSER, SimpleServiceApiErrorParser.INSTANCE);
    }
}
