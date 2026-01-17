package com.nicehash.clients.examples.marketmaker.event;

public class XTimerEvent implements Event<Long> {

  private long time = System.currentTimeMillis();

  public XTimerEvent() {}

  public XTimerEvent(long time) {
    this.time = time;
  }

  @Override
  public Long getApiEvent() {
    return time;
  }

  @Override
  public EventType type() {
    return EventType.TIMER;
  }
}
