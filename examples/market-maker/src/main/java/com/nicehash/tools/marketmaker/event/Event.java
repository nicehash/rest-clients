package com.nicehash.tools.marketmaker.event;

public interface Event {

    EventType type();

    Object getApiEvent();
}
