package com.nicehash.clients.exchange.domain.market;

import com.nicehash.clients.exchange.constant.ExchangeConstants;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** Order book of a symbol in NiceHash. */
public class OrderBookSync implements AutoCloseable {

  /** Sync handle - impl detail. */
  private final SyncHandle handle;

  /** Last update id of this order book. */
  private long lastUpdateId;

  /** List of bids (price/qty). */
  private final Map<BigDecimal, BigDecimal> bids;

  /** List of asks (price/qty). */
  private final Map<BigDecimal, BigDecimal> asks;

  public OrderBookSync(
      SyncHandle handle, Map<BigDecimal, BigDecimal> bids, Map<BigDecimal, BigDecimal> asks) {
    this.handle = Objects.requireNonNull(handle);
    this.bids = bids;
    this.asks = asks;
  }

  public long getLastUpdateId() {
    return lastUpdateId;
  }

  public void setLastUpdateId(long lastUpdateId) {
    this.lastUpdateId = lastUpdateId;
  }

  /**
   * Be aware that this is not atomic operation with regard to getAsks()!! As such, bids and asks
   * may come from different updates. Use snapshot() for atomic operation.
   */
  public Map<BigDecimal, BigDecimal> getBids() {
    handle.readLock().lock();
    try {
      Map<BigDecimal, BigDecimal> map = new TreeMap<>(Comparator.reverseOrder());
      map.putAll(bids);
      return map;
    } finally {
      handle.readLock().unlock();
    }
  }

  /**
   * Be aware that this is not atomic operation with regard to getBids()!! As such, asks and bids
   * may come from different updates. Use snapshot() for atomic operation.
   */
  public Map<BigDecimal, BigDecimal> getAsks() {
    handle.readLock().lock();
    try {
      return new TreeMap<>(asks);
    } finally {
      handle.readLock().unlock();
    }
  }

  public OrderBook snapshot() {
    OrderBook ob = new OrderBook();
    handle.readLock().lock();
    try {
      List<OrderBookEntry> bidsList =
          bids.entrySet().stream()
              .map(e -> new OrderBookEntry(e.getKey(), e.getValue()))
              .collect(Collectors.toList());
      ob.setBids(bidsList);

      List<OrderBookEntry> asksList =
          asks.entrySet().stream()
              .map(e -> new OrderBookEntry(e.getKey(), e.getValue()))
              .collect(Collectors.toList());
      ob.setAsks(asksList);
      return ob;
    } finally {
      handle.readLock().unlock();
    }
  }

  @Override
  public void close() throws IOException {
    handle.close();
  }

  @Override
  public String toString() {
    handle.readLock().lock();
    try {
      return new ToStringBuilder(this, ExchangeConstants.TO_STRING_BUILDER_STYLE)
          .append("lastUpdateId", lastUpdateId)
          .append("bids", getBids())
          .append("asks", getAsks())
          .toString();
    } finally {
      handle.readLock().unlock();
    }
  }
}
