package com.nicehash.tools.marketmaker.event;

public class XTimerEvent implements Event {

    public final long time = System.currentTimeMillis();

    @Override
    public Long getApiEvent() {
        return time;
    }

    @Override
    public EventType type() {
        return EventType.TIMER;
    }
}
