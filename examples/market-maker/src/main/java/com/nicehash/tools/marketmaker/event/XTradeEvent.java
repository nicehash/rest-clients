package com.nicehash.tools.marketmaker.event;

import com.nicehash.exchange.client.domain.event.OrderTradeEvent;

public class XTradeEvent implements Event {

    private final OrderTradeEvent e;

    public XTradeEvent(OrderTradeEvent e) {
        this.e = e;
    }

    @Override
    public OrderTradeEvent getApiEvent() {
        return e;
    }

    @Override
    public EventType type() {
        return EventType.TRADE;
    }
}
