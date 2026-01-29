package com.nicehash.clients.examples.marketmaker.event;

import com.nicehash.clients.exchange.domain.market.OrderBook;

public class XOrderEvent implements Event<OrderBook> {

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
