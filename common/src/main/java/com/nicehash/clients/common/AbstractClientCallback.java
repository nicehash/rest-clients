package com.nicehash.clients.common;

public abstract class AbstractClientCallback<T> implements ClientCallback<T> {
  @Override
  public void onFailure(Throwable t) {
    throw new ClientException("Async callback failure.", t);
  }
}
