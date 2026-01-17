package com.nicehash.clients.exchange.domain.account;

import com.nicehash.clients.exchange.constant.ExchangeConstants;
import com.nicehash.clients.exchange.domain.OrderSide;
import com.nicehash.clients.exchange.domain.OrderType;
import com.nicehash.clients.exchange.domain.TimeInForce;
import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** A trade order to enter or exit a position. */
public class NewOrder {

  /** Symbol to place the order on. */
  private String symbol;

  /** Buy/Sell order side. */
  private OrderSide side;

  /** Type of order. */
  private OrderType type;

  /** Time in force to indicate how long will the order remain active. */
  private TimeInForce timeInForce;

  /** Quantity. */
  private BigDecimal quantity;

  /** Price. */
  private BigDecimal price;

  /** A unique id for the order. Automatically generated if not sent. */
  private String clientOrderId;

  /** Used with stop orders. */
  private BigDecimal stopPrice;

  /** Used with market buy. */
  private BigDecimal marketMaxAmount;

  /** Used with iceberg orders. */
  private BigDecimal icebergQty;

  /** Receiving window. */
  private Long recvWindow;

  /** Order timestamp. */
  private long timestamp;

  /** Creates a new order with all required parameters. */
  public NewOrder(
      String symbol, OrderSide side, OrderType type, TimeInForce timeInForce, BigDecimal quantity) {
    this.symbol = symbol;
    this.side = side;
    this.type = type;
    this.timeInForce = timeInForce;
    this.quantity = quantity;
    this.timestamp = System.currentTimeMillis();
    this.recvWindow = ExchangeConstants.DEFAULT_RECEIVING_WINDOW;
  }

  /**
   * Creates a new order with all required parameters plus price, which is optional for MARKET
   * orders.
   */
  public NewOrder(
      String symbol,
      OrderSide side,
      OrderType type,
      TimeInForce timeInForce,
      BigDecimal quantity,
      BigDecimal price) {
    this(symbol, side, type, timeInForce, quantity);
    this.price = price;
  }

  public String getSymbol() {
    return symbol;
  }

  public NewOrder symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }

  public OrderSide getSide() {
    return side;
  }

  public NewOrder side(OrderSide side) {
    this.side = side;
    return this;
  }

  public OrderType getType() {
    return type;
  }

  public NewOrder type(OrderType type) {
    this.type = type;
    return this;
  }

  public TimeInForce getTimeInForce() {
    return timeInForce;
  }

  public NewOrder timeInForce(TimeInForce timeInForce) {
    this.timeInForce = timeInForce;
    return this;
  }

  public BigDecimal getQuantity() {
    return quantity;
  }

  public NewOrder quantity(BigDecimal quantity) {
    this.quantity = quantity;
    return this;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public NewOrder price(BigDecimal price) {
    this.price = price;
    return this;
  }

  public String getClientOrderId() {
    return clientOrderId;
  }

  public NewOrder clientOrderId(String clientOrderId) {
    this.clientOrderId = clientOrderId;
    return this;
  }

  public BigDecimal getStopPrice() {
    return stopPrice;
  }

  public NewOrder stopPrice(BigDecimal stopPrice) {
    this.stopPrice = stopPrice;
    return this;
  }

  public BigDecimal getMarketMaxAmount() {
    return marketMaxAmount;
  }

  public NewOrder marketMaxAmount(BigDecimal marketMaxAmount) {
    this.marketMaxAmount = marketMaxAmount;
    return this;
  }

  public BigDecimal getIcebergQty() {
    return icebergQty;
  }

  public NewOrder icebergQty(BigDecimal icebergQty) {
    this.icebergQty = icebergQty;
    return this;
  }

  public Long getRecvWindow() {
    return recvWindow;
  }

  public NewOrder recvWindow(Long recvWindow) {
    this.recvWindow = recvWindow;
    return this;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public NewOrder timestamp(long timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Places a MARKET buy order for the given <code>quantity</code>.
   *
   * @return a new order which is pre-configured with MARKET as the order type and BUY as the order
   *     side.
   */
  public static NewOrder marketBuy(String symbol, double marketMaxAmount) {
    return marketBuy(symbol, BigDecimal.valueOf(marketMaxAmount));
  }

  /**
   * Places a MARKET buy order for the given <code>quantity</code>.
   *
   * @return a new order which is pre-configured with MARKET as the order type and BUY as the order
   *     side.
   */
  public static NewOrder marketBuy(String symbol, double quantity, double marketMaxAmount) {
    return marketBuy(symbol, BigDecimal.valueOf(quantity), BigDecimal.valueOf(marketMaxAmount));
  }

  /**
   * Places a MARKET sell order for the given <code>quantity</code>.
   *
   * @return a new order which is pre-configured with MARKET as the order type and SELL as the order
   *     side.
   */
  public static NewOrder marketSell(String symbol, double quantity) {
    return new NewOrder(
        symbol, OrderSide.SELL, OrderType.MARKET, null, BigDecimal.valueOf(quantity));
  }

  /**
   * Places a LIMIT buy order for the given <code>quantity</code> and <code>price</code>.
   *
   * @return a new order which is pre-configured with LIMIT as the order type and BUY as the order
   *     side.
   */
  public static NewOrder limitBuy(
      String symbol, TimeInForce timeInForce, double quantity, double price) {
    return new NewOrder(
        symbol,
        OrderSide.BUY,
        OrderType.LIMIT,
        timeInForce,
        BigDecimal.valueOf(quantity),
        BigDecimal.valueOf(price));
  }

  /**
   * Places a LIMIT sell order for the given <code>quantity</code> and <code>price</code>.
   *
   * @return a new order which is pre-configured with LIMIT as the order type and SELL as the order
   *     side.
   */
  public static NewOrder limitSell(
      String symbol, TimeInForce timeInForce, double quantity, double price) {
    return new NewOrder(
        symbol,
        OrderSide.SELL,
        OrderType.LIMIT,
        timeInForce,
        BigDecimal.valueOf(quantity),
        BigDecimal.valueOf(price));
  }

  // With BigDecimal

  /**
   * Places a MARKET buy order for the given <code>quantity</code>.
   *
   * @return a new order which is pre-configured with MARKET as the order type and BUY as the order
   *     side.
   */
  public static NewOrder marketBuy(String symbol, BigDecimal marketMaxAmount) {
    return marketBuy(symbol, null, marketMaxAmount);
  }

  /**
   * Places a MARKET buy order for the given <code>quantity</code>.
   *
   * @return a new order which is pre-configured with MARKET as the order type and BUY as the order
   *     side.
   */
  public static NewOrder marketBuy(String symbol, BigDecimal quantity, BigDecimal marketMaxAmount) {
    return new NewOrder(symbol, OrderSide.BUY, OrderType.MARKET, null, quantity)
        .marketMaxAmount(marketMaxAmount);
  }

  /**
   * Places a MARKET sell order for the given <code>quantity</code>.
   *
   * @return a new order which is pre-configured with MARKET as the order type and SELL as the order
   *     side.
   */
  public static NewOrder marketSell(String symbol, BigDecimal quantity) {
    return new NewOrder(symbol, OrderSide.SELL, OrderType.MARKET, null, quantity);
  }

  /**
   * Places a LIMIT buy order for the given <code>quantity</code> and <code>price</code>.
   *
   * @return a new order which is pre-configured with LIMIT as the order type and BUY as the order
   *     side.
   */
  public static NewOrder limitBuy(
      String symbol, TimeInForce timeInForce, BigDecimal quantity, BigDecimal price) {
    return new NewOrder(symbol, OrderSide.BUY, OrderType.LIMIT, timeInForce, quantity, price);
  }

  /**
   * Places a LIMIT sell order for the given <code>quantity</code> and <code>price</code>.
   *
   * @return a new order which is pre-configured with LIMIT as the order type and SELL as the order
   *     side.
   */
  public static NewOrder limitSell(
      String symbol, TimeInForce timeInForce, BigDecimal quantity, BigDecimal price) {
    return new NewOrder(symbol, OrderSide.SELL, OrderType.LIMIT, timeInForce, quantity, price);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ExchangeConstants.TO_STRING_BUILDER_STYLE)
        .append("symbol", symbol)
        .append("side", side)
        .append("type", type)
        .append("timeInForce", timeInForce)
        .append("quantity", quantity)
        .append("price", price)
        .append("clientOrderId", clientOrderId)
        .append("stopPrice", stopPrice)
        .append("icebergQty", icebergQty)
        .append("recvWindow", recvWindow)
        .append("timestamp", timestamp)
        .toString();
  }
}
