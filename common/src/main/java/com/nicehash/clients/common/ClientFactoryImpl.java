package com.nicehash.clients.common;

import com.nicehash.clients.common.spi.ClientImplementation;
import com.nicehash.clients.common.spi.Options;
import com.nicehash.clients.util.options.OptionMap;
import java.lang.reflect.Constructor;

class ClientFactoryImpl implements ClientFactory {

  private final OptionMap options;

  public ClientFactoryImpl(OptionMap options) {
    this.options = options;
  }

  @Override
  public <T> T getClient(Class<T> clientClass) {
    return getClient(clientClass, options);
  }

  @Override
  public <T> T getClient(Class<T> clientClass, String token) {
    OptionMap.Builder builder = OptionMap.builder();
    builder.addAll(options);
    builder.set(Options.TOKEN, token);
    OptionMap options = builder.getMap();
    return getClient(clientClass, options);
  }

  @Override
  public <T> T getClient(Class<T> clientClass, String key, String secret) {
    OptionMap.Builder builder = OptionMap.builder();
    builder.addAll(options);
    builder.set(Options.KEY, key);
    builder.set(Options.SECRET, secret);
    OptionMap options = builder.getMap();
    return getClient(clientClass, options);
  }

  @Override
  public <T> T getClient(Class<T> clientClass, OptionMap original) {
    ClientImplementation ci = clientClass.getAnnotation(ClientImplementation.class);
    if (ci == null) {
      throw new IllegalArgumentException(
          String.format(
              "Client class %s is missing @ClientImplementation annotation.",
              clientClass.getName()));
    }
    Class<?> implClass = ci.value();
    if (!clientClass.isAssignableFrom(implClass)) {
      throw new IllegalArgumentException(
          String.format("%S is not assignable from client class %s", implClass, clientClass));
    }
    // Add required / implicit options...
    OptionMap implicit =
        OptionMap.builder().set(Options.NAME, clientClass.getSimpleName()).getMap();

    OptionMap.Builder builder = OptionMap.builder();
    builder.addAll(options);
    builder.addAll(original);
    builder.addAll(implicit);
    OptionMap current = builder.getMap();

    try {
      @SuppressWarnings("unchecked")
      Constructor<T> ctor = (Constructor<T>) implClass.getConstructor(OptionMap.class);
      return ctor.newInstance(current);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
