package com.nicehash.clients.examples.marketmaker.event;

import com.nicehash.clients.exchange.domain.event.CandlestickEvent;

public class XCandlestickEvent implements Event<CandlestickEvent> {

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
