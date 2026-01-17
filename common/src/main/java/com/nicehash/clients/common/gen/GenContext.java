package com.nicehash.clients.common.gen;

import com.nicehash.clients.common.spi.ServiceApiErrorParser;
import okhttp3.Call;

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
