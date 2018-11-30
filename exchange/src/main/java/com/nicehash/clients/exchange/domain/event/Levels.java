package com.nicehash.clients.exchange.domain.event;

/**
 * Levels
 */
public enum Levels {
    L5(5),
    L10(10),
    L20(20);

    private int x;

    Levels(int x) {
        this.x = x;
    }

    public int get() {
        return x;
    }
}
