package com.nicehash.clients.exchange.domain.account.request;

import com.nicehash.clients.exchange.constant.ExchangeConstants;
import java.util.UUID;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** Request object for canceling an order. */
public class CancelOrderRequest extends OrderRequest {

  private UUID orderId;

  private String clientOrderId;

  public CancelOrderRequest(String symbol, UUID orderId) {
    super(symbol);
    this.orderId = orderId;
  }

  public CancelOrderRequest(String symbol, String clientOrderId) {
    super(symbol);
    this.clientOrderId = clientOrderId;
  }

  public UUID getOrderId() {
    return orderId;
  }

  public CancelOrderRequest orderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  public String getClientOrderId() {
    return clientOrderId;
  }

  public CancelOrderRequest clientOrderId(String clientOrderId) {
    this.clientOrderId = clientOrderId;
    return this;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ExchangeConstants.TO_STRING_BUILDER_STYLE)
        .append("orderId", orderId)
        .append("clientOrderId", clientOrderId)
        .toString();
  }
}
