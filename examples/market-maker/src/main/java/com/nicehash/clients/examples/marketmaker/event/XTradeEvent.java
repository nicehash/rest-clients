package com.nicehash.clients.examples.marketmaker.event;

import com.nicehash.clients.exchange.domain.event.OrderTradeEvent;

public class XTradeEvent implements Event<OrderTradeEvent> {

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
