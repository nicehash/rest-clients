package com.nicehash.clients.domain;

import java.time.Year;

public enum AggregatedPeriod {
    NONE(""),
    DAY("day"),
    MONTH("month"),
    QUARTER("quarter"),
    YEAR("year");

    private final String name;

    AggregatedPeriod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
