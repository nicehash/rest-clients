package com.nicehash.clients.common.spi;

/**
 * @author Ales Justin
 */
public interface ServiceHandle<T> {
    Class<T> getServiceInterface();
}
