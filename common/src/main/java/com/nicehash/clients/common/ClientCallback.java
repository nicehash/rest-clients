package com.nicehash.clients.common;

public interface ClientCallback<T> {
  void onResponse(T result);

  void onFailure(Throwable t);
}
