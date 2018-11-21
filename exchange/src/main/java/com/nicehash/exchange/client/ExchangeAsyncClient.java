package com.nicehash.exchange.client;

import com.nicehash.common.domain.OrderRelOp;
import com.nicehash.exchange.client.domain.SortDirection;
import com.nicehash.exchange.client.domain.account.Account;
import com.nicehash.exchange.client.domain.account.FeeStatus;
import com.nicehash.exchange.client.domain.account.NewOrder;
import com.nicehash.exchange.client.domain.account.NewOrderResponse;
import com.nicehash.exchange.client.domain.account.Order;
import com.nicehash.exchange.client.domain.account.Trade;
import com.nicehash.exchange.client.domain.account.TradeHistoryItem;
import com.nicehash.exchange.client.domain.account.request.AllOrdersRequest;
import com.nicehash.exchange.client.domain.account.request.CancelOrderRequest;
import com.nicehash.exchange.client.domain.account.request.OrderRequest;
import com.nicehash.exchange.client.domain.account.request.OrderStatusRequest;
import com.nicehash.exchange.client.domain.event.ListenKey;
import com.nicehash.exchange.client.domain.general.Asset;
import com.nicehash.exchange.client.domain.general.ExchangeInfo;
import com.nicehash.exchange.client.domain.general.ServerTime;
import com.nicehash.exchange.client.domain.market.BookTicker;
import com.nicehash.exchange.client.domain.market.Candlestick;
import com.nicehash.exchange.client.domain.market.CandlestickInterval;
import com.nicehash.exchange.client.domain.market.OrderBook;
import com.nicehash.exchange.client.domain.market.TickerPrice;
import com.nicehash.exchange.client.domain.market.TickerStatistic;
import com.nicehash.exchange.client.impl.ExchangeAsyncClientImpl;
import com.nicehash.external.ClientCallback;
import com.nicehash.external.spi.ClientImplementation;

import java.util.List;
import java.util.UUID;

/**
 * NiceHash Exchange API façade, supporting asynchronous/non-blocking access NiceHash's Exchange REST API.
 */
@ClientImplementation(ExchangeAsyncClientImpl.class)
public interface ExchangeAsyncClient {

    // General endpoints

    /**
     * Test connectivity to the Rest API.
     */
    void ping(ClientCallback<Void> callback);

    /**
     * Check server time.
     */
    void getServerTime(ClientCallback<ServerTime> callback);

    /**
     * Current exchange trading rules and symbol information
     */
    void getExchangeInfo(ClientCallback<ExchangeInfo> callback);

    /**
     * ALL supported assets and whether or not they can be withdrawn.
     */
    void getAllAssets(ClientCallback<List<Asset>> callback);

    // Market Data endpoints

    /**
     * Get order book of a symbol (asynchronous)
     *
     * @param symbol   ticker symbol (e.g. ETHBTC)
     * @param limit    depth of the order book (max 100)
     * @param callback the callback that handles the response
     */
    void getOrderBook(String symbol, Integer limit, ClientCallback<OrderBook> callback);

    /**
     * Get recent trades (up to last 500). Weight: 1
     *
     * @param symbol   ticker symbol (e.g. ETHBTC)
     * @param limit    of last trades (Default 500; max 500.)
     * @param callback the callback that handles the response
     */
    void getTrades(String symbol, Integer limit, ClientCallback<List<TradeHistoryItem>> callback);

    /**
     * Get older trades. Weight: 5
     *
     * @param symbol            ticker symbol (e.g. ETHBTC)
     * @param limit             of last trades (Default 500; max 500.)
     * @param fromEventNumber   eventNumber to fetch from. Default gets most recent trades.
     * @param callback          the callback that handles the response
     */
    void getHistoricalTrades(String symbol, Integer limit, Long fromEventNumber, ClientCallback<List<TradeHistoryItem>> callback);

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
//     * @param callback  the callback that handles the response
//     * @return a list of aggregate trades for the given symbol
//     */
//    void getAggTrades(String symbol, String fromId, Integer limit, Long startTime, Long endTime, ClientCallback<List<AggTrade>> callback);
//
//    /**
//     * Return the most recent aggregate trades for <code>symbol</code>
//     *
//     * @see #getAggTrades(String, String, Integer, Long, Long, ClientCallback)
//     */
//    void getAggTrades(String symbol, ClientCallback<List<AggTrade>> callback);

    /**
     * Kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.
     *
     * @param symbol    symbol to aggregate (mandatory)
     * @param interval  candlestick interval (mandatory)
     * @param limit     Default 500; max 500 (optional)
     * @param startTime Timestamp in ms to get candlestick bars from INCLUSIVE (optional).
     * @param endTime   Timestamp in ms to get candlestick bars until INCLUSIVE (optional).
     * @param callback  the callback that handles the response containing a candlestick bar for the given symbol and interval
     */
    void getCandlestickBars(String symbol, CandlestickInterval interval, Integer limit, Long startTime, Long endTime, ClientCallback<List<Candlestick>> callback);

    /**
     * Kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.
     *
     * @see #getCandlestickBars(String, CandlestickInterval, ClientCallback)
     */
    void getCandlestickBars(String symbol, CandlestickInterval interval, ClientCallback<List<Candlestick>> callback);

    /**
     * Get 24 hour price change statistics (asynchronous).
     *
     * @param symbol   ticker symbol (e.g. ETHBTC)
     * @param callback the callback that handles the response
     */
    void get24HrPriceStatistics(String symbol, ClientCallback<TickerStatistic> callback);

    /**
     * Get 24 hour price change statistics for all symbols (asynchronous).
     *
     * @param callback the callback that handles the response
     */
    void getAll24HrPriceStatistics(ClientCallback<List<TickerStatistic>> callback);

    /**
     * Get Latest price for all symbols (asynchronous).
     *
     * @param callback the callback that handles the response
     */
    void getAllPrices(ClientCallback<List<TickerPrice>> callback);

    /**
     * Get latest price for <code>symbol</code> (asynchronous).
     *
     * @param symbol   ticker symbol (e.g. ETHBTC)
     * @param callback the callback that handles the response
     */
    void getPrice(String symbol, ClientCallback<TickerPrice> callback);

    /**
     * Get best price/qty on the order book for all symbols (asynchronous).
     *
     * @param callback the callback that handles the response
     */
    void getBookTickers(ClientCallback<List<BookTicker>> callback);

    /**
     * Get best price/qty on the order book for all symbols (asynchronous).
     *
     * @param symbol   ticker symbol (e.g. ETHBTC)
     * @param callback the callback that handles the response
     */
    void getBookTicker(String symbol, ClientCallback<BookTicker> callback);

    // Account endpoints

    /**
     * Send in a new order (asynchronous)
     *
     * @param order    the new order to submit.
     * @param callback the callback that handles the response
     */
    void newOrder(NewOrder order, ClientCallback<NewOrderResponse> callback);

    /**
     * Test new order creation and signature/recvWindow long. Creates and validates a new order but does not send it into the matching engine.
     *
     * @param order    the new TEST order to submit.
     * @param callback the callback that handles the response
     */
    void newOrderTest(NewOrder order, ClientCallback<Void> callback);

    /**
     * Check an order's status (asynchronous).
     *
     * @param orderStatusRequest order status request parameters
     * @param callback           the callback that handles the response
     */
    void getOrderStatus(OrderStatusRequest orderStatusRequest, ClientCallback<Order> callback);

    /**
     * Cancel an active order (asynchronous).
     *
     * @param cancelOrderRequest order status request parameters
     * @param callback           the callback that handles the response
     */
    void cancelOrder(CancelOrderRequest cancelOrderRequest, ClientCallback<Void> callback);

    /**
     * Get all open orders on a symbol (asynchronous).
     *
     * @param orderRequest order request parameters
     * @param callback     the callback that handles the response
     */
    void getOpenOrders(OrderRequest orderRequest, ClientCallback<List<Order>> callback);

    /**
     * Get all account orders; active, canceled, or filled.
     *
     * @param orderRequest order request parameters
     * @param callback     the callback that handles the response
     */
    void getAllOrders(AllOrdersRequest orderRequest, ClientCallback<List<Order>> callback);

    /**
     * Get all trades for order.
     *
     * @param symbol        the symbol
     * @param orderId       the order id
     * @param clientOrderId the client order id
     * @param callback      the callback that handles response
     */
    void getOrderTrades(String symbol, UUID orderId, String clientOrderId, ClientCallback<List<Trade>> callback);

    /**
     * Get current account information (async).
     */
    void getAccount(Long recvWindow, Long timestamp, ClientCallback<Account> callback);

    /**
     * Get current account information using default parameters (async).
     */
    void getAccount(ClientCallback<Account> callback);

    /**
     * Get current fee status information using default parameters (async).
     */
    void getFeeStatus(ClientCallback<FeeStatus> callback);

    /**
     * Get trades for a specific account and symbol.
     *
     * @param symbol symbol to get trades from
     * @param relationalOp the relation to tradeNumber
     * @param tradeNumber tradeNumber to fetch from. Default gets most recent trades.
     * @param limit  default 500; max 500
     * @param sortDirection the sort direction
     * @param callback          the callback that handles the response with a list of trades
     */
    void getMyTrades(String symbol, OrderRelOp relationalOp, Long tradeNumber, Integer limit, SortDirection sortDirection, Long recvWindow, Long timestamp, ClientCallback<List<Trade>> callback);

    /**
     * Get trades for a specific account and symbol.
     *
     * @param symbol   symbol to get trades from
     * @param limit    default 500; max 500
     * @param callback the callback that handles the response with a list of trades
     */
    void getMyTrades(String symbol, Integer limit, ClientCallback<List<Trade>> callback);

    /**
     * Get trades for a specific account and symbol.
     *
     * @param symbol   symbol to get trades from
     * @param callback the callback that handles the response with a list of trades
     */
    void getMyTrades(String symbol, ClientCallback<List<Trade>> callback);

    // User stream endpoints

    /**
     * Start a new user data stream.
     *
     * @param callback the callback that handles the response which contains a listenKey
     */
    void startUserDataStream(ClientCallback<ListenKey> callback);

    /**
     * PING a user data stream to prevent a time out.
     *
     * @param listenKey listen key that identifies a data stream
     * @param callback  the callback that handles the response which contains a listenKey
     */
    void keepAliveUserDataStream(String listenKey, ClientCallback<Void> callback);

    /**
     * Close out a new user data stream.
     *
     * @param listenKey listen key that identifies a data stream
     * @param callback  the callback that handles the response which contains a listenKey
     */
    void closeUserDataStream(String listenKey, ClientCallback<Void> callback);
}