package com.nicehash.tools.marketmaker.event;

import com.nicehash.exchange.client.domain.market.OrderBook;

public class XOrderEvent implements Event {

    OrderBook e;

    public XOrderEvent(OrderBook e) {
        this.e = e;
    }

    @Override
    public OrderBook getApiEvent() {
        return e;
    }

    @Override
    public EventType type() {
        return EventType.ORDER;
    }
}
