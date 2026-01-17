package com.nicehash.clients.common;

import com.nicehash.clients.util.options.OptionMap;

public interface ClientFactory {

  <T> T getClient(Class<T> clientClass);

  <T> T getClient(Class<T> clientClass, String token);

  <T> T getClient(Class<T> clientClass, String key, String secret);

  <T> T getClient(Class<T> clientClass, OptionMap options);
}
