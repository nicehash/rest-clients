package com.nicehash.external.spi;

import com.nicehash.utils.options.OptionMap;
import okhttp3.OkHttpClient;

/**
 * @author Ales Justin
 */
public class NoopServiceBuilder extends AbstractServiceBuilder {
    @Override
    protected void buildHttpClient(OptionMap options, OkHttpClient.Builder builder) {
    }
}
