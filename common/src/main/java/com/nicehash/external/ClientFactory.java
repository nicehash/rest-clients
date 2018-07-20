package com.nicehash.external;

import com.nicehash.utils.options.OptionMap;

/**
 * @author Ales Justin
 */
public interface ClientFactory {

    <T> T getClient(Class<T> clientClass);

    <T> T getClient(Class<T> clientClass, String token);

    <T> T getClient(Class<T> clientClass, String key, String secret);

    <T> T getClient(Class<T> clientClass, OptionMap options);
}
