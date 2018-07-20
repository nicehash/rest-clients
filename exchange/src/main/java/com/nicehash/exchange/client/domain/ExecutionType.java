package com.nicehash.exchange.client.domain;

/**
 * Order execution type.
 */
public enum ExecutionType {
    NEW,
    CANCELED,
    REPLACED,
    REJECTED,
    TRADE,
    EXPIRED
}