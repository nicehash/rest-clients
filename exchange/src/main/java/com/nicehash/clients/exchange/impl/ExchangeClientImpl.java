package com.nicehash.clients.exchange.impl;

import com.nicehash.clients.domain.OrderRelOp;
import com.nicehash.clients.exchange.ExchangeClient;
import com.nicehash.clients.exchange.constant.ExchangeConstants;
import com.nicehash.clients.exchange.domain.general.Asset;
import com.nicehash.clients.exchange.domain.general.ExchangeInfo;
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
import com.nicehash.clients.exchange.domain.market.AggTrade;
import com.nicehash.clients.exchange.domain.market.BookTicker;
import com.nicehash.clients.exchange.domain.market.Candlestick;
import com.nicehash.clients.exchange.domain.market.CandlestickInterval;
import com.nicehash.clients.exchange.domain.market.OrderBook;
import com.nicehash.clients.exchange.domain.market.TickerPrice;
import com.nicehash.clients.exchange.domain.market.TickerStatistic;
import com.nicehash.clients.common.gen.ClientGenerator;
import com.nicehash.clients.common.spi.ServiceHandle;
import com.nicehash.clients.util.options.OptionMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.nicehash.clients.common.gen.ClientGenerator.executeSync;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Implementation of Exchange's REST API using Retrofit with synchronous/blocking method calls.
 */
public class ExchangeClientImpl implements ExchangeClient, ServiceHandle<ExchangeService> {

    private final ExchangeService service;
    protected final Logger log = LoggerFactory.getLogger(ExchangeClientImpl.class);

    public ExchangeClientImpl(OptionMap options) throws Exception {
        service = ClientGenerator.createService(ExchangeService.class, options, log);
    }

    @Override
    public Class<ExchangeService> getServiceInterface() {
        return ExchangeService.class;
    }

    @Override
    public void close() {
        ClientGenerator.close(getServiceInterface());
    }

    // General endpoints

    @Override
    public void ping() {
        executeSync(getServiceInterface(), service.ping());
    }

    @Override
    public Long getServerTime() {
        return executeSync(getServiceInterface(), service.getServerTime()).getServerTime();
    }

    @Override
    public ExchangeInfo getExchangeInfo() {
        return executeSync(getServiceInterface(), service.getExchangeInfo());
    }

    @Override
    public List<Asset> getAllAssets() {
        return executeSync(getServiceInterface(), service.getAllAssets("")).getList(); // TODO
    }

    // Market Data endpoints

    @Override
    public OrderBook getOrderBook(String symbol, Integer limit) {
        return executeSync(getServiceInterface(), service.getOrderBook(symbol, limit));
    }

    @Override
    public List<TradeHistoryItem> getTrades(String symbol, Integer limit) {
        return executeSync(getServiceInterface(), service.getTrades(symbol, limit)).getList();
    }

    @Override
    public List<TradeHistoryItem> getHistoricalTrades(String symbol, Integer limit, Long fromId) {
        return executeSync(getServiceInterface(), service.getHistoricalTrades(symbol, limit, fromId)).getList();
    }

    //@Override
    public List<AggTrade> getAggTrades(String symbol, String fromId, Integer limit, Long startTime, Long endTime) {
        return executeSync(getServiceInterface(), service.getAggTrades(symbol, fromId, limit, startTime, endTime)).getList();
    }

    //@Override
    public List<AggTrade> getAggTrades(String symbol) {
        return getAggTrades(symbol, null, null, null, null);
    }

    @Override
    public List<Candlestick> getCandlestickBars(String symbol, CandlestickInterval interval, Integer limit, Long startTime, Long endTime) {
        return executeSync(getServiceInterface(), service.getCandlestickBars(symbol, interval.getIntervalId(), limit, startTime, endTime)).getList();
    }

    @Override
    public List<Candlestick> getCandlestickBars(String symbol, CandlestickInterval interval) {
        return getCandlestickBars(symbol, interval, null, null, null);
    }

    @Override
    public List<TickerStatistic> getAll24HrPriceStatistics() {
        return executeSync(getServiceInterface(), service.get24HrPriceStatistics(null)).getList();
    }

    @Override
    public TickerStatistic get24HrPriceStatistics(String symbol) {
        return executeSync(getServiceInterface(), service.get24HrPriceStatistics(Objects.requireNonNull(symbol))).getSingle();
    }

    @Override
    public List<TickerPrice> getAllPrices() {
        return executeSync(getServiceInterface(), service.getLatestPrice(null)).getList();
    }

    @Override
    public TickerPrice getPrice(String symbol) {
        return executeSync(getServiceInterface(), service.getLatestPrice(Objects.requireNonNull(symbol))).getSingle();
    }

    @Override
    public List<BookTicker> getBookTickers() {
        return executeSync(getServiceInterface(), service.getBookTicker(null)).getList();
    }

    @Override
    public BookTicker getBookTicker(String symbol) {
        return executeSync(getServiceInterface(), service.getBookTicker(Objects.requireNonNull(symbol))).getSingle();
    }

    @Override
    public NewOrderResponse newOrder(NewOrder order) {
        return executeSync(getServiceInterface(), service.newOrder(order.getSymbol(), order.getSide(), order.getType(), order.getTimeInForce(),
                                                                   order.getQuantity(), order.getPrice(), order.getClientOrderId(), order.getStopPrice(),
                                                                   order.getMarketMaxAmount(), order.getIcebergQty(), order.getRecvWindow(), order.getTimestamp()));
    }

    @Override
    public void newOrderTest(NewOrder order) {
        executeSync(getServiceInterface(), service.newOrderTest(order.getSymbol(), order.getSide(), order.getType(), order.getTimeInForce(),
                                                                order.getQuantity(), order.getPrice(), order.getClientOrderId(), order.getStopPrice(),
                                                                order.getMarketMaxAmount(), order.getIcebergQty(), order.getRecvWindow(), order.getTimestamp()));
    }

    // Account endpoints

    @Override
    public Order getOrderStatus(OrderStatusRequest orderStatusRequest) {
        return executeSync(getServiceInterface(), service.getOrderStatus(orderStatusRequest.getSymbol(),
            orderStatusRequest.getOrderId(), orderStatusRequest.getOrigClientOrderId(),
            orderStatusRequest.getRecvWindow(), orderStatusRequest.getTimestamp()));
    }

    @Override
    public void cancelOrder(CancelOrderRequest cancelOrderRequest) {
        executeSync(getServiceInterface(), service.cancelOrder(cancelOrderRequest.getSymbol(),
                                                               cancelOrderRequest.getOrderId(), cancelOrderRequest.getClientOrderId(),
                                                               cancelOrderRequest.getRecvWindow(), cancelOrderRequest.getTimestamp()));
    }

    @Override
    public List<Order> getOpenOrders(OrderRequest orderRequest) {
        return executeSync(getServiceInterface(), service.getOpenOrders(orderRequest.getSymbol(), orderRequest.getSortDirection(), orderRequest.getRecvWindow(), orderRequest.getTimestamp())).getList();
    }

    @Override
    public List<Order> getAllOrders(AllOrdersRequest orderRequest) {
        return executeSync(getServiceInterface(), service.getAllOrders(orderRequest.getSymbol(), orderRequest.getRelationalOp(), orderRequest.getSubmitNumber(),
                                                                       orderRequest.getSubmitTime(), orderRequest.getTerminated(),
                                                                       orderRequest.getLimit(), orderRequest.getSortDirection(),
                                                                       orderRequest.getRecvWindow(), orderRequest.getTimestamp())
        ).getList();
    }

    @Override
    public List<Trade> getOrderTrades(String symbol, UUID orderId, String clientOrderId) {
        return executeSync(getServiceInterface(), service.getOrderTrades(symbol, orderId, clientOrderId, ExchangeConstants.DEFAULT_RECEIVING_WINDOW, System.currentTimeMillis())).getList();
    }

    @Override
    public Account getAccount(Long recvWindow, Long timestamp) {
        return executeSync(getServiceInterface(), service.getAccount(recvWindow, timestamp));
    }

    @Override
    public Account getAccount() {
        return getAccount(ExchangeConstants.DEFAULT_RECEIVING_WINDOW, System.currentTimeMillis());
    }

    @Override
    public FeeStatus getFeeStatus() {
        return executeSync(getServiceInterface(), service.getFeeStatus(ExchangeConstants.DEFAULT_RECEIVING_WINDOW, System.currentTimeMillis()));
    }

    @Override
    public List<Trade> getMyTrades(String symbol, OrderRelOp relationalOp, Long tradeNumber, Integer limit, SortDirection sortDirection, Long recvWindow, Long timestamp) {
        return executeSync(getServiceInterface(), service.getMyTrades(symbol, relationalOp, tradeNumber, limit, sortDirection, recvWindow, timestamp)).getList();
    }

    @Override
    public List<Trade> getMyTrades(String symbol, Integer limit) {
        return getMyTrades(symbol, null, null, limit, null, ExchangeConstants.DEFAULT_RECEIVING_WINDOW, System.currentTimeMillis());
    }

    @Override
    public List<Trade> getMyTrades(String symbol) {
        return getMyTrades(symbol, null, null, null, null, ExchangeConstants.DEFAULT_RECEIVING_WINDOW, System.currentTimeMillis());
    }

    // User stream endpoints

    @Override
    public String startUserDataStream() {
        return executeSync(getServiceInterface(), service.startUserDataStream()).toString();
    }

    @Override
    public void keepAliveUserDataStream(String listenKey) {
        executeSync(getServiceInterface(), service.keepAliveUserDataStream(listenKey));
    }

    @Override
    public void closeUserDataStream(String listenKey) {
        executeSync(getServiceInterface(), service.closeAliveUserDataStream(listenKey));
    }
}
