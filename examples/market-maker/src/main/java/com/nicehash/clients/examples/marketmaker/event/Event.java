package com.nicehash.clients.examples.marketmaker.event;

public interface Event<T> {

  EventType type();

  T getApiEvent();
}
