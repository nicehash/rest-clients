package com.nicehash.clients.common;

import com.nicehash.clients.util.options.OptionMap;

public final class Clients {
  public static ClientFactory factory() {
    return factory(OptionMap.EMPTY);
  }

  public static ClientFactory factory(OptionMap options) {
    return new ClientFactoryImpl(options);
  }

  private Clients() {
  }
}
