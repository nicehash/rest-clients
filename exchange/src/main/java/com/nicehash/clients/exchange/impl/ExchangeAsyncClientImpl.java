package com.nicehash.clients.exchange.impl;

import com.nicehash.clients.domain.OrderRelOp;
import com.nicehash.clients.exchange.ExchangeAsyncClient;
import com.nicehash.clients.exchange.constant.ExchangeConstants;
import com.nicehash.clients.exchange.domain.general.Asset;
import com.nicehash.clients.exchange.domain.general.ExchangeInfo;
import com.nicehash.clients.exchange.domain.general.ServerTime;
import com.nicehash.clients.exchange.domain.ListObject;
import com.nicehash.clients.exchange.domain.SingleOrListObject;
import com.nicehash.clients.exchange.domain.SortDirection;
import com.nicehash.clients.exchange.domain.account.Account;
import com.nicehash.clients.exchange.domain.account.FeeStatus;
import com.nicehash.clients.exchange.domain.account.NewOrder;
import com.nicehash.clients.exchange.domain.account.NewOrderResponse;
import com.nicehash.clients.exchange.domain.account.Order;
import com.nicehash.clients.exchange.domain.account.Trade;
import com.nicehash.clients.exchange.domain.account.TradeHistoryItem;
import com.nicehash.clients.exchange.domain.account.request.AllOrdersRequest;
import com.nicehash.clients.exchange.domain.account.request.CancelOrderRequest;
import com.nicehash.clients.exchange.domain.account.request.OrderRequest;
import com.nicehash.clients.exchange.domain.account.request.OrderStatusRequest;
import com.nicehash.clients.exchange.domain.event.ListenKey;
import com.nicehash.clients.exchange.domain.market.AggTrade;
import com.nicehash.clients.exchange.domain.market.BookTicker;
import com.nicehash.clients.exchange.domain.market.Candlestick;
import com.nicehash.clients.exchange.domain.market.CandlestickInterval;
import com.nicehash.clients.exchange.domain.market.OrderBook;
import com.nicehash.clients.exchange.domain.market.TickerPrice;
import com.nicehash.clients.exchange.domain.market.TickerStatistic;
import com.nicehash.clients.common.ClientCallback;
import com.nicehash.clients.common.gen.ClientGenerator;
import com.nicehash.clients.common.spi.ServiceHandle;
import com.nicehash.clients.util.options.OptionMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.nicehash.clients.common.gen.ClientGenerator.executeAsync;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of Exchange's REST API using Retrofit with asynchronous/non-blocking method calls.
 */
public class ExchangeAsyncClientImpl implements ExchangeAsyncClient, ServiceHandle<ExchangeService> {

    private final ExchangeService service;
    protected final Logger log = LoggerFactory.getLogger(ExchangeAsyncClientImpl.class);

    public ExchangeAsyncClientImpl(OptionMap options) throws Exception {
        service = ClientGenerator.createService(ExchangeService.class, options, log);
    }

    private <U extends SingleOrListObject<T>, T> ClientCallback<U> wrapSingle(ClientCallback<T> callback) {
        return new ClientCallback<U>() {
            @Override
            public void onResponse(U result) {
                callback.onResponse(result.getSingle());
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        };
    }

    private <U extends ListObject<T>, T> ClientCallback<U> wrapList(ClientCallback<List<T>> callback) {
        return new ClientCallback<U>() {
            @Override
            public void onResponse(U result) {
                callback.onResponse(result.getList());
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        };
    }

    @Override
    public Class<ExchangeService> getServiceInterface() {
        return ExchangeService.class;
    }

    // General endpoints

    @Override
    public void ping(ClientCallback<Void> callback) {
        executeAsync(getServiceInterface(), service.ping(), callback);
    }

    @Override
    public void getServerTime(ClientCallback<ServerTime> callback) {
        executeAsync(getServiceInterface(), service.getServerTime(), callback);
    }

    @Override
    public void getExchangeInfo(ClientCallback<ExchangeInfo> callback) {
        executeAsync(getServiceInterface(), service.getExchangeInfo(), callback);
    }

    @Override
    public void getAllAssets(ClientCallback<List<Asset>> callback) {
        executeAsync(getServiceInterface(), service.getAllAssets(""), wrapList(callback)); // TODO
    }

    // Market Data endpoints

    @Override
    public void getOrderBook(String symbol, Integer limit, ClientCallback<OrderBook> callback) {
        executeAsync(getServiceInterface(), service.getOrderBook(symbol, limit), callback);
    }

    @Override
    public void getTrades(String symbol, Integer limit, ClientCallback<List<TradeHistoryItem>> callback) {
        executeAsync(getServiceInterface(), service.getTrades(symbol, limit), wrapList(callback));
    }

    @Override
    public void getHistoricalTrades(String symbol, Integer limit, Long tradeNumber, ClientCallback<List<TradeHistoryItem>> callback) {
        executeAsync(getServiceInterface(), service.getHistoricalTrades(symbol, limit, tradeNumber), wrapList(callback));
    }

    //@Override
    public void getAggTrades(String symbol, String fromId, Integer limit, Long startTime, Long endTime, ClientCallback<List<AggTrade>> callback) {
        executeAsync(getServiceInterface(), service.getAggTrades(symbol, fromId, limit, startTime, endTime), wrapList(callback));
    }

    //@Override
    public void getAggTrades(String symbol, ClientCallback<List<AggTrade>> callback) {
        getAggTrades(symbol, null, null, null, null, callback);
    }

    @Override
    public void getCandlestickBars(String symbol, CandlestickInterval interval, Integer limit, Long startTime, Long endTime, ClientCallback<List<Candlestick>> callback) {
        executeAsync(getServiceInterface(), service.getCandlestickBars(symbol, interval.getIntervalId(), limit, startTime, endTime), wrapList(callback));
    }

    @Override
    public void getCandlestickBars(String symbol, CandlestickInterval interval, ClientCallback<List<Candlestick>> callback) {
        getCandlestickBars(symbol, interval, null, null, null, callback);
    }

    @Override
    public void get24HrPriceStatistics(String symbol, ClientCallback<TickerStatistic> callback) {
        executeAsync(getServiceInterface(), service.get24HrPriceStatistics(symbol), wrapSingle(callback));
    }

    @Override
    public void getAll24HrPriceStatistics(ClientCallback<List<TickerStatistic>> callback) {
        executeAsync(getServiceInterface(), service.get24HrPriceStatistics(null), wrapList(callback));
    }

    @Override
    public void getAllPrices(ClientCallback<List<TickerPrice>> callback) {
        executeAsync(getServiceInterface(), service.getLatestPrice(null), wrapList(callback));
    }

    @Override
    public void getPrice(String symbol, ClientCallback<TickerPrice> callback) {
        executeAsync(getServiceInterface(), service.getLatestPrice(symbol), wrapSingle(callback));
    }

    @Override
    public void getBookTickers(ClientCallback<List<BookTicker>> callback) {
        executeAsync(getServiceInterface(), service.getBookTicker(null), wrapList(callback));
    }

    @Override
    public void getBookTicker(String symbol, ClientCallback<BookTicker> callback) {
        executeAsync(getServiceInterface(), service.getBookTicker(symbol), wrapSingle(callback));
    }

    @Override
    public void newOrder(NewOrder order, ClientCallback<NewOrderResponse> callback) {
        executeAsync(getServiceInterface(), service.newOrder(order.getSymbol(), order.getSide(), order.getType(), order.getTimeInForce(),
                                                             order.getQuantity(), order.getPrice(), order.getClientOrderId(), order.getStopPrice(), order.getMarketMaxAmount(),
                                                             order.getIcebergQty(), order.getRecvWindow(), order.getTimestamp()), callback);
    }

    @Override
    public void newOrderTest(NewOrder order, ClientCallback<Void> callback) {
        executeAsync(getServiceInterface(), service.newOrderTest(order.getSymbol(), order.getSide(), order.getType(), order.getTimeInForce(),
                                                                 order.getQuantity(), order.getPrice(), order.getClientOrderId(), order.getStopPrice(), order.getMarketMaxAmount(),
                                                                 order.getIcebergQty(), order.getRecvWindow(), order.getTimestamp()), callback);
    }

    // Account endpoints

    @Override
    public void getOrderStatus(OrderStatusRequest orderStatusRequest, ClientCallback<Order> callback) {
        executeAsync(getServiceInterface(), service.getOrderStatus(orderStatusRequest.getSymbol(),
            orderStatusRequest.getOrderId(), orderStatusRequest.getOrigClientOrderId(),
            orderStatusRequest.getRecvWindow(), orderStatusRequest.getTimestamp()), callback);
    }

    @Override
    public void cancelOrder(CancelOrderRequest cancelOrderRequest, ClientCallback<Void> callback) {
        executeAsync(getServiceInterface(), service.cancelOrder(cancelOrderRequest.getSymbol(),
                                                                cancelOrderRequest.getOrderId(), cancelOrderRequest.getClientOrderId(),
                                                                cancelOrderRequest.getRecvWindow(), cancelOrderRequest.getTimestamp()), callback);
    }

    @Override
    public void getOpenOrders(OrderRequest orderRequest, ClientCallback<List<Order>> callback) {
        executeAsync(getServiceInterface(), service.getOpenOrders(orderRequest.getSymbol(), orderRequest.getSortDirection(),
                                                                  orderRequest.getRecvWindow(), orderRequest.getTimestamp()), wrapList(callback));
    }

    @Override
    public void getAllOrders(AllOrdersRequest orderRequest, ClientCallback<List<Order>> callback) {
        executeAsync(getServiceInterface(), service.getAllOrders(orderRequest.getSymbol(), orderRequest.getRelationalOp(), orderRequest.getSubmitNumber(),
                                                                 orderRequest.getSubmitTime(), orderRequest.getTerminated(),
                                                                 orderRequest.getLimit(), orderRequest.getSortDirection(),
                                                                 orderRequest.getRecvWindow(), orderRequest.getTimestamp()), wrapList(callback));
    }

    @Override
    public void getOrderTrades(String symbol, UUID orderId, String clientOrderId, ClientCallback<List<Trade>> callback) {
        executeAsync(getServiceInterface(), service.getOrderTrades(symbol, orderId, clientOrderId, ExchangeConstants.DEFAULT_RECEIVING_WINDOW, System.currentTimeMillis()), wrapList(callback));
    }

    @Override
    public void getAccount(Long recvWindow, Long timestamp, ClientCallback<Account> callback) {
        executeAsync(getServiceInterface(), service.getAccount(recvWindow, timestamp), callback);
    }

    @Override
    public void getAccount(ClientCallback<Account> callback) {
        long timestamp = System.currentTimeMillis();
        executeAsync(getServiceInterface(), service.getAccount(ExchangeConstants.DEFAULT_RECEIVING_WINDOW, timestamp), callback);
    }

    @Override
    public void getFeeStatus(ClientCallback<FeeStatus> callback) {
        long timestamp = System.currentTimeMillis();
        executeAsync(getServiceInterface(), service.getFeeStatus(ExchangeConstants.DEFAULT_RECEIVING_WINDOW, timestamp), callback);
    }

    @Override
    public void getMyTrades(String symbol, OrderRelOp relationalOp, Long tradeNumber, Integer limit, SortDirection sortDirection, Long recvWindow, Long timestamp, ClientCallback<List<Trade>> callback) {
        executeAsync(getServiceInterface(), service.getMyTrades(symbol, relationalOp, tradeNumber, limit, sortDirection, recvWindow, timestamp), wrapList(callback));
    }

    @Override
    public void getMyTrades(String symbol, Integer limit, ClientCallback<List<Trade>> callback) {
        getMyTrades(symbol, null, null, limit, null, ExchangeConstants.DEFAULT_RECEIVING_WINDOW, System.currentTimeMillis(), callback);
    }

    @Override
    public void getMyTrades(String symbol, ClientCallback<List<Trade>> callback) {
        getMyTrades(symbol, null, null, null, null, ExchangeConstants.DEFAULT_RECEIVING_WINDOW, System.currentTimeMillis(), callback);
    }

    // User stream endpoints

    @Override
    public void startUserDataStream(ClientCallback<ListenKey> callback) {
        executeAsync(getServiceInterface(), service.startUserDataStream(), callback);
    }

    @Override
    public void keepAliveUserDataStream(String listenKey, ClientCallback<Void> callback) {
        executeAsync(getServiceInterface(), service.keepAliveUserDataStream(listenKey), callback);
    }

    @Override
    public void closeUserDataStream(String listenKey, ClientCallback<Void> callback) {
        executeAsync(getServiceInterface(), service.closeAliveUserDataStream(listenKey), callback);
    }
}
