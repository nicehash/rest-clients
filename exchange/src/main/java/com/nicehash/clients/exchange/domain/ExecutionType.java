package com.nicehash.clients.exchange.domain;

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