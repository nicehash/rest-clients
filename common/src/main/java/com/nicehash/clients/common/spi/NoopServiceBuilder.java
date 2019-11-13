package com.nicehash.clients.common.spi;

import com.nicehash.clients.util.options.OptionMap;
import okhttp3.OkHttpClient;


public class NoopServiceBuilder extends AbstractServiceBuilder {
    @Override
    protected void buildHttpClient(OptionMap options, OkHttpClient.Builder builder) {
    }
}
