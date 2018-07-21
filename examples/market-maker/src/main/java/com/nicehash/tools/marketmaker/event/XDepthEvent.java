package com.nicehash.tools.marketmaker.event;

import com.nicehash.exchange.client.domain.event.DepthEvent;

public class XDepthEvent implements Event {

    private final DepthEvent e;

    public XDepthEvent(DepthEvent e) {
        this.e = e;
    }

    @Override
    public DepthEvent getApiEvent() {
        return e;
    }

    @Override
    public EventType type() {
        return EventType.DEPTH;
    }
}
