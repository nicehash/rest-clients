package com.nicehash.clients.exchange.domain.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nicehash.clients.exchange.constant.ExchangeConstants;
import com.nicehash.clients.exchange.domain.OrderSide;
import com.nicehash.clients.exchange.domain.OrderStatus;
import com.nicehash.clients.exchange.domain.OrderType;
import com.nicehash.clients.exchange.domain.TimeInForce;
import java.math.BigDecimal;
import java.util.UUID;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** Trade order information. */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

  /** Symbol that the order was put on. */
  private String symbol;

  /** Order id. */
  private UUID orderId;

  /** Client order id. */
  private String clientOrderId;

  /** Price. */
  private BigDecimal price;

  /** Original quantity. */
  private BigDecimal origQty;

  /** Executed quantity. */
  private BigDecimal executedQty;

  /** Executed secondary quantity. */
  private BigDecimal executedSndQty;

  /** Order status. */
  private OrderStatus status;

  /** Time in force to indicate how long will the order remain active. */
  private TimeInForce timeInForce;

  /** Type of order. */
  private OrderType type;

  /** Buy/Sell order side. */
  private OrderSide side;

  /** Used with stop orders. */
  private BigDecimal stopPrice;

  /** Used with iceberg orders. */
  private BigDecimal icebergQty;

  /** Order submission timestamp. */
  private long submitTime;

  /**
   * Submit number is unique within a particular market counted from 0 up - it orders individual
   * orders by their submission to the market (topic) - it is actually the offset of the request
   * message in the kafka topic.
   */
  private Long submitNumber;

  /** The response time of order's last response. */
  private Long lastResponseTime;

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

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal getOrigQty() {
    return origQty;
  }

  public void setOrigQty(BigDecimal origQty) {
    this.origQty = origQty;
  }

  public BigDecimal getExecutedQty() {
    return executedQty;
  }

  public void setExecutedQty(BigDecimal executedQty) {
    this.executedQty = executedQty;
  }

  public BigDecimal getExecutedSndQty() {
    return executedSndQty;
  }

  public void setExecutedSndQty(BigDecimal executedSndQty) {
    this.executedSndQty = executedSndQty;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public TimeInForce getTimeInForce() {
    return timeInForce;
  }

  public void setTimeInForce(TimeInForce timeInForce) {
    this.timeInForce = timeInForce;
  }

  public OrderType getType() {
    return type;
  }

  public void setType(OrderType type) {
    this.type = type;
  }

  public OrderSide getSide() {
    return side;
  }

  public void setSide(OrderSide side) {
    this.side = side;
  }

  public BigDecimal getStopPrice() {
    return stopPrice;
  }

  public void setStopPrice(BigDecimal stopPrice) {
    this.stopPrice = stopPrice;
  }

  public BigDecimal getIcebergQty() {
    return icebergQty;
  }

  public void setIcebergQty(BigDecimal icebergQty) {
    this.icebergQty = icebergQty;
  }

  public long getSubmitTime() {
    return submitTime;
  }

  public void setSubmitTime(long submitTime) {
    this.submitTime = submitTime;
  }

  public Long getSubmitNumber() {
    return submitNumber;
  }

  public void setSubmitNumber(Long submitNumber) {
    this.submitNumber = submitNumber;
  }

  public Long getLastResponseTime() {
    return lastResponseTime;
  }

  public void setLastResponseTime(Long lastResponseTime) {
    this.lastResponseTime = lastResponseTime;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ExchangeConstants.TO_STRING_BUILDER_STYLE)
        .append("symbol", symbol)
        .append("orderId", orderId)
        .append("clientOrderId", clientOrderId)
        .append("price", price)
        .append("origQty", origQty)
        .append("executedQty", executedQty)
        .append("executedSndQty", executedSndQty)
        .append("status", status)
        .append("timeInForce", timeInForce)
        .append("type", type)
        .append("side", side)
        .append("stopPrice", stopPrice)
        .append("icebergQty", icebergQty)
        .append("submitTime", submitTime)
        .append("submitNumber", submitNumber)
        .append("lastResponseTime", lastResponseTime)
        .toString();
  }
}
