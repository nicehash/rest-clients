package com.nicehash.clients.exchange.domain.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Trade event.
 *
 * @see UserDataUpdateEvent
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderTradeEvent {

  @JsonProperty("e")
  private String eventType;

  @JsonProperty("E")
  private long eventTime;

  @JsonProperty("s")
  private String symbol;

  @JsonProperty("t")
  private int tradeNumber;

  /** Price. */
  @JsonProperty("p")
  private String price;

  /** Quantity. */
  @JsonProperty("q")
  private String quantity;

  @JsonProperty("b")
  private long buyerNumber;

  @JsonProperty("a")
  private long sellerNumber;

  /** Order/trade time. */
  @JsonProperty("T")
  private long tradeTime;

  @JsonProperty("m")
  private boolean marketMaker;

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public long getEventTime() {
    return eventTime;
  }

  public void setEventTime(long eventTime) {
    this.eventTime = eventTime;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public int getTradeNumber() {
    return tradeNumber;
  }

  public void setTradeNumber(int tradeNumber) {
    this.tradeNumber = tradeNumber;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getQuantity() {
    return quantity;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }

  public long getBuyerNumber() {
    return buyerNumber;
  }

  public void setBuyerNumber(long buyerNumber) {
    this.buyerNumber = buyerNumber;
  }

  public long getSellerNumber() {
    return sellerNumber;
  }

  public void setSellerNumber(long sellerNumber) {
    this.sellerNumber = sellerNumber;
  }

  public long getTradeTime() {
    return tradeTime;
  }

  public void setTradeTime(Long tradeTime) {
    this.tradeTime = tradeTime;
  }

  public boolean isMarketMaker() {
    return marketMaker;
  }

  public void setMarketMaker(boolean marketMaker) {
    this.marketMaker = marketMaker;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("eventType", eventType)
        .append("eventTime", eventTime)
        .append("symbol", symbol)
        .append("price", price)
        .append("quantity", quantity)
        .toString();
  }
}
