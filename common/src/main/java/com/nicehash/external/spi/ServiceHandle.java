package com.nicehash.external.spi;

/**
 * @author Ales Justin
 */
public interface ServiceHandle<T> {
    Class<T> getServiceInterface();
}
