package com.nicehash.exchange.client;

import com.nicehash.exchange.client.domain.RelationalOp;
import com.nicehash.exchange.client.domain.SortDirection;
import com.nicehash.exchange.client.domain.account.Account;
import com.nicehash.exchange.client.domain.account.NewOrder;
import com.nicehash.exchange.client.domain.account.NewOrderResponse;
import com.nicehash.exchange.client.domain.account.Order;
import com.nicehash.exchange.client.domain.account.Trade;
import com.nicehash.exchange.client.domain.account.TradeHistoryItem;
import com.nicehash.exchange.client.domain.account.request.AllOrdersRequest;
import com.nicehash.exchange.client.domain.account.request.CancelOrderRequest;
import com.nicehash.exchange.client.domain.account.request.OrderRequest;
import com.nicehash.exchange.client.domain.account.request.OrderStatusRequest;
import com.nicehash.exchange.client.domain.general.Asset;
import com.nicehash.exchange.client.domain.general.ExchangeInfo;
import com.nicehash.exchange.client.domain.market.BookTicker;
import com.nicehash.exchange.client.domain.market.Candlestick;
import com.nicehash.exchange.client.domain.market.CandlestickInterval;
import com.nicehash.exchange.client.domain.market.OrderBook;
import com.nicehash.exchange.client.domain.market.TickerPrice;
import com.nicehash.exchange.client.domain.market.TickerStatistic;
import com.nicehash.exchange.client.impl.ExchangeClientImpl;
import com.nicehash.external.spi.ClientImplementation;

import java.io.Closeable;
import java.util.List;
import java.util.UUID;

/**
 * NiceHash Exchange client.
 */
@ClientImplementation(ExchangeClientImpl.class)
public interface ExchangeClient extends Closeable {

    // General endpoints

    /**
     * Test connectivity to the Rest API.
     */
    void ping();

    /**
     * Test connectivity to the Rest API and get the current server time.
     *
     * @return current server time.
     */
    Long getServerTime();

    /**
     * @return Current exchange trading rules and symbol information
     */
    ExchangeInfo getExchangeInfo();

    /**
     * @return All the supported assets and whether or not they can be withdrawn.
     */
    List<Asset> getAllAssets();

    // Market Data endpoints

    /**
     * Get order book of a symbol.
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     * @param limit  depth of the order book (max 100)
     */
    OrderBook getOrderBook(String symbol, Integer limit);

    /**
     * Get recent trades (up to last 500). Weight: 1
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     * @param limit  of last trades (Default 500; max 500.)
     */
    List<TradeHistoryItem> getTrades(String symbol, Integer limit);

    /**
     * Get older trades. Weight: 5
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     * @param limit  of last trades (Default 500; max 500.)
     * @param tradeNumber tradeNumber to fetch from. Default gets most recent trades.
     */
    List<TradeHistoryItem> getHistoricalTrades(String symbol, Integer limit, Long tradeNumber);

//    /**
//     * Get compressed, aggregate trades. Trades that fill at the time, from the same order, with
//     * the same price will have the quantity aggregated.
//     *
//     * If both <code>startTime</code> and <code>endTime</code> are sent, <code>limit</code>should not
//     * be sent AND the distance between <code>startTime</code> and <code>endTime</code> must be less than 24 hours.
//     *
//     * @param symbol    symbol to aggregate (mandatory)
//     * @param fromId    ID to get aggregate trades from INCLUSIVE (optional)
//     * @param limit     Default 500; max 500 (optional)
//     * @param startTime Timestamp in ms to get aggregate trades from INCLUSIVE (optional).
//     * @param endTime   Timestamp in ms to get aggregate trades until INCLUSIVE (optional).
//     * @return a list of aggregate trades for the given symbol
//     */
//    List<AggTrade> getAggTrades(String symbol, String fromId, Integer limit, Long startTime, Long endTime);
//
//    /**
//     * Return the most recent aggregate trades for <code>symbol</code>
//     *
//     * @see #getAggTrades(String, String, Integer, Long, Long)
//     */
//    List<AggTrade> getAggTrades(String symbol);

    /**
     * Kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.
     *
     * @param symbol    symbol to aggregate (mandatory)
     * @param interval  candlestick interval (mandatory)
     * @param limit     Default 500; max 500 (optional)
     * @param startTime Timestamp in ms to get candlestick bars from INCLUSIVE (optional).
     * @param endTime   Timestamp in ms to get candlestick bars until INCLUSIVE (optional).
     * @return a candlestick bar for the given symbol and interval
     */
    List<Candlestick> getCandlestickBars(String symbol, CandlestickInterval interval, Integer limit, Long startTime, Long endTime);

    /**
     * Kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.
     *
     * @see #getCandlestickBars(String, CandlestickInterval, Integer, Long, Long)
     */
    List<Candlestick> getCandlestickBars(String symbol, CandlestickInterval interval);

    /**
     * Get 24 hour price change statistics.
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     */
    TickerStatistic get24HrPriceStatistics(String symbol);

    /**
     * Get 24 hour price change statistics for all symbols.
     */
    List<TickerStatistic> getAll24HrPriceStatistics();

    /**
     * Get Latest price for all symbols.
     */
    List<TickerPrice> getAllPrices();

    /**
     * Get latest price for <code>symbol</code>.
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     */
    TickerPrice getPrice(String symbol);

    /**
     * Get best price/qty on the order book for all symbols.
     */
    List<BookTicker> getBookTickers();

    /**
     * Get best price/qty on the order book for all symbols.
     */
    BookTicker getBookTicker(String symbol);

    // Account endpoints

    /**
     * Send in a new order.
     *
     * @param order the new order to submit.
     * @return a response containing details about the newly placed order.
     */
    NewOrderResponse newOrder(NewOrder order);

    /**
     * Test new order creation and signature/recvWindow long. Creates and validates a new order but does not send it into the matching engine.
     *
     * @param order the new TEST order to submit.
     */
    void newOrderTest(NewOrder order);

    /**
     * Check an order's status.
     *
     * @param orderStatusRequest order status request options/filters
     * @return an order
     */
    Order getOrderStatus(OrderStatusRequest orderStatusRequest);

    /**
     * Cancel an active order.
     *
     * @param cancelOrderRequest order status request parameters
     */
    void cancelOrder(CancelOrderRequest cancelOrderRequest);

    /**
     * Get all open orders on a symbol.
     *
     * @param orderRequest order request parameters
     * @return a list of all account open orders on a symbol.
     */
    List<Order> getOpenOrders(OrderRequest orderRequest);

    /**
     * Get all account orders; active, canceled, or filled.
     *
     * @param orderRequest order request parameters
     * @return a list of all account orders
     */
    List<Order> getAllOrders(AllOrdersRequest orderRequest);

    /**
     * Get trades for a specific account and symbol.
     *
     * @param symbol        symbol to get trades from
     * @param orderId       the order id to get trades
     * @param clientOrderId client order id to get trades
     * @return a list of order's trades
     */
    List<Trade> getOrderTrades(String symbol, UUID orderId, String clientOrderId);

    /**
     * Get current account information.
     */
    Account getAccount(Long recvWindow, Long timestamp);

    /**
     * Get current account information using default parameters.
     */
    Account getAccount();

    /**
     * Get trades for a specific account and symbol.
     *
     * @param symbol symbol to get trades from
     * @param relationalOp the relation to tradeNumber
     * @param tradeNumber tradeNumber to fetch from. Default gets most recent trades.
     * @param limit  default 500; max 500
     * @param sortDirection the sort direction
     * @return a list of trades
     */
    List<Trade> getMyTrades(String symbol, RelationalOp relationalOp, Long tradeNumber, Integer limit, SortDirection sortDirection, Long recvWindow, Long timestamp);

    /**
     * Get trades for a specific account and symbol.
     *
     * @param symbol symbol to get trades from
     * @param limit  default 500; max 500
     * @return a list of trades
     */
    List<Trade> getMyTrades(String symbol, Integer limit);

    /**
     * Get trades for a specific account and symbol.
     *
     * @param symbol symbol to get trades from
     * @return a list of trades
     */
    List<Trade> getMyTrades(String symbol);

    // User stream endpoints

    /**
     * Start a new user data stream.
     *
     * @return a listen key that can be used with data streams
     */
    String startUserDataStream();

    /**
     * PING a user data stream to prevent a time out.
     *
     * @param listenKey listen key that identifies a data stream
     */
    void keepAliveUserDataStream(String listenKey);

    /**
     * Close out a new user data stream.
     *
     * @param listenKey listen key that identifies a data stream
     */
    void closeUserDataStream(String listenKey);
}
