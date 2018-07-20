package com.nicehash.external.gen;

import com.nicehash.external.spi.ServiceApiErrorParser;
import okhttp3.Call;

/**
 * @author Ales Justin
 */
class GenContext {
    private Call.Factory factory;
    private ServiceApiErrorParser parser;

    public GenContext(Call.Factory factory, ServiceApiErrorParser parser) {
        this.factory = factory;
        this.parser = parser;
    }

    public Call.Factory getFactory() {
        return factory;
    }

    public ServiceApiErrorParser getParser() {
        return parser;
    }
}
