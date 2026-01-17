package com.nicehash.clients.exchange.domain.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nicehash.clients.exchange.constant.ExchangeConstants;
import java.math.BigDecimal;
import java.util.UUID;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** Represents an executed trade history item. */
public class TradeHistoryItem {
  /**
   * Trade identifier is a combination of UUID (which is actually the corresponding order id) and
   * additional int idNumber (counted from 0 up).
   */
  private UUID id;

  private int idNumber;

  /** Price. */
  private BigDecimal price;

  /** Quantity. */
  private BigDecimal qty;

  /** Secondary Quantity. */
  private BigDecimal sndQty;

  /** Trade execution time. */
  private long time;

  @JsonProperty("isBuyer")
  private boolean buyer;

  @JsonProperty("isMaker")
  private boolean maker;

  @JsonProperty("isBestMatch")
  private boolean bestMatch;

  /**
   * Trade number is unique within a particular market segment (symbol) counted from 0 up - it
   * orders individual trades by their execution in the whole market segment.
   */
  private long tradeNumber;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public int getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(int idNumber) {
    this.idNumber = idNumber;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public BigDecimal getSndQty() {
    return sndQty;
  }

  public void setSndQty(BigDecimal sndQty) {
    this.sndQty = sndQty;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public boolean isBuyer() {
    return buyer;
  }

  public void setBuyer(boolean buyer) {
    this.buyer = buyer;
  }

  public boolean isMaker() {
    return maker;
  }

  public void setMaker(boolean maker) {
    this.maker = maker;
  }

  public boolean isBestMatch() {
    return bestMatch;
  }

  public void setBestMatch(boolean bestMatch) {
    this.bestMatch = bestMatch;
  }

  public long getTradeNumber() {
    return tradeNumber;
  }

  public void setTradeNumber(long tradeNumber) {
    this.tradeNumber = tradeNumber;
  }

  @Override
  public final String toString() {
    return toStringBuilder().toString();
  }

  protected ToStringBuilder toStringBuilder() {
    return new ToStringBuilder(this, ExchangeConstants.TO_STRING_BUILDER_STYLE)
        .append("id", id)
        .append("idNumber", idNumber)
        .append("price", price)
        .append("qty", qty)
        .append("sndQty", sndQty)
        .append("time", time)
        .append("isBuyer", buyer)
        .append("isMaker", maker)
        .append("isBestMatch", bestMatch)
        .append("tradeNumber", tradeNumber);
  }
}
