package com.nicehash.exchange.client.domain;

/**
 * Status of a submitted order.
 */
public enum OrderStatus {
    UNCONFIRMED,
    ENTERED,
    PARTIALLY_FILLED,
    FILLED,
    CANCELED,
    REJECTED,
    EXPIRED
}
