package com.nicehash.clients.common;

/**
 * @author Ales Justin
 */
public interface ClientCallback<T> {
    void onResponse(T result);

    void onFailure(Throwable t);
}
