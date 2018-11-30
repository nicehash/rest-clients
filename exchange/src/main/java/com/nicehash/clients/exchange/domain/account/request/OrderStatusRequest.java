package com.nicehash.clients.exchange.domain.account.request;

import java.util.UUID;

import com.nicehash.clients.exchange.constant.ExchangeConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A specialized order request with additional filters.
 */
public class OrderStatusRequest extends OrderRequest {

    private UUID orderId;

    private String origClientOrderId;

    public OrderStatusRequest(String symbol, UUID orderId) {
        super(symbol);
        this.orderId = orderId;
    }

    public OrderStatusRequest(String symbol, String origClientOrderId) {
        super(symbol);
        this.origClientOrderId = origClientOrderId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public OrderStatusRequest orderId(UUID orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getOrigClientOrderId() {
        return origClientOrderId;
    }

    public OrderStatusRequest origClientOrderId(String origClientOrderId) {
        this.origClientOrderId = origClientOrderId;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ExchangeConstants.TO_STRING_BUILDER_STYLE)
            .append("orderId", orderId)
            .append("origClientOrderId", origClientOrderId)
            .toString();
    }
}
