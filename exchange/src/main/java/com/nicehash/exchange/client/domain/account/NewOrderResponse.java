package com.nicehash.exchange.client.domain.account;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nicehash.exchange.client.constant.ExchangeConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Response returned when placing a new order on the system.
 *
 * @see NewOrder for the request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewOrderResponse {

    /**
     * Order symbol.
     */
    private String symbol;

    /**
     * Order id.
     */
    private UUID orderId;

    /**
     * This will be either a generated one, or the newClientOrderId parameter
     * which was passed when creating the new order.
     */
    private String clientOrderId;

    /**
     * Transact time for this order.
     */
    private Long transactTime;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public Long getTransactTime() {
        return transactTime;
    }

    public void setTransactTime(Long transactTime) {
        this.transactTime = transactTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ExchangeConstants.TO_STRING_BUILDER_STYLE)
            .append("symbol", symbol)
            .append("orderId", orderId)
            .append("clientOrderId", clientOrderId)
            .append("transactTime", transactTime)
            .toString();
    }
}
