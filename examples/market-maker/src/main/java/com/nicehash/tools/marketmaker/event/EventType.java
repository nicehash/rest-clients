package com.nicehash.tools.marketmaker.event;

public enum EventType {
    DEPTH(XDepthEvent.class),
    ORDER(XOrderEvent.class),
    CANDLESTICK(XCandlestickEvent.class),
    TRADE(XTradeEvent.class),
    TIMER(XTimerEvent.class),
    TICKERS(XTickersEvent.class);

    private Class clazz;

    public Class getType() {
        return clazz;
    }

    private EventType(Class clazz) {
        this.clazz = clazz;
    }
}
