package com.nicehash.tools.marketmaker.event;

public interface Event<T> {

    EventType type();

    T getApiEvent();
}
