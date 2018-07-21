package com.nicehash.tools.marketmaker.event;

import com.nicehash.exchange.client.domain.event.AllMarketTickersEvent;

public class XTickersEvent implements Event {

    private final AllMarketTickersEvent e;

    public XTickersEvent(AllMarketTickersEvent e) {
        this.e = e;
    }

    @Override
    public AllMarketTickersEvent getApiEvent() {
        return e;
    }

    @Override
    public EventType type() {
        return EventType.TICKERS;
    }
}
