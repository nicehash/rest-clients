package com.nicehash.clients.exchange.domain.market;

import com.nicehash.clients.exchange.constant.ExchangeConstants;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** Order book of a symbol in NiceHash. */
public class OrderBook {

  /** Last update id of this order book. */
  private long lastUpdateId;

  /** List of bids (price/qty). */
  private List<OrderBookEntry> bids;

  /** List of asks (price/qty). */
  private List<OrderBookEntry> asks;

  public long getLastUpdateId() {
    return lastUpdateId;
  }

  public void setLastUpdateId(long lastUpdateId) {
    this.lastUpdateId = lastUpdateId;
  }

  public List<OrderBookEntry> getBids() {
    return bids;
  }

  public void setBids(List<OrderBookEntry> bids) {
    this.bids = bids;
  }

  public List<OrderBookEntry> getAsks() {
    return asks;
  }

  public void setAsks(List<OrderBookEntry> asks) {
    this.asks = asks;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ExchangeConstants.TO_STRING_BUILDER_STYLE)
        .append("lastUpdateId", lastUpdateId)
        .append("bids", bids)
        .append("asks", asks)
        .toString();
  }
}
