package com.nicehash.clients.examples.marketmaker.event;

import com.nicehash.clients.exchange.domain.event.AllMarketTickersEvent;

public class XTickersEvent implements Event<AllMarketTickersEvent> {

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
