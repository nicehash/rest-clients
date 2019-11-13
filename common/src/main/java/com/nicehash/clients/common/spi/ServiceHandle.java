package com.nicehash.clients.common.spi;


public interface ServiceHandle<T> {
    Class<T> getServiceInterface();
}
