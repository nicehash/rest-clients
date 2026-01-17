package com.nicehash.clients.examples.marketmaker.event;

import com.nicehash.clients.exchange.domain.event.DepthEvent;

public class XDepthEvent implements Event<DepthEvent> {

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
