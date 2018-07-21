package com.nicehash.tools.marketmaker.event;

import com.nicehash.exchange.client.domain.event.CandlestickEvent;

public class XCandlestickEvent implements Event {

    private final CandlestickEvent e;

    public XCandlestickEvent(CandlestickEvent e) {
        this.e = e;
    }

    @Override
    public CandlestickEvent getApiEvent() {
        return e;
    }

    @Override
    public EventType type() {
        return EventType.CANDLESTICK;
    }
}
