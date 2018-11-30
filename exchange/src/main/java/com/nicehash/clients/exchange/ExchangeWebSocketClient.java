package com.nicehash.clients.exchange;

import com.nicehash.clients.exchange.domain.event.AllMarketTickersEvent;
import com.nicehash.clients.exchange.domain.event.CandlestickEvent;
import com.nicehash.clients.exchange.domain.event.DepthEvent;
import com.nicehash.clients.exchange.domain.event.Levels;
import com.nicehash.clients.exchange.domain.event.OrderTradeEvent;
import com.nicehash.clients.exchange.domain.event.UserDataUpdateEvent;
import com.nicehash.clients.exchange.domain.market.CandlestickInterval;
import com.nicehash.clients.exchange.domain.market.OrderBook;
import com.nicehash.clients.common.ClientCallback;

import java.io.Closeable;

/**
 * NiceHash Exchange API data streaming façade, supporting streaming of events through web sockets.
 */
public interface ExchangeWebSocketClient extends Closeable {

    /**
     * Open a new web socket to receive {@link OrderBook orderBookEvents} on a callback.
     *
     * @param symbol   the market symbol to subscribe to
     * @param levels   the levels limit value
     * @param callback the callback to call on new events
     * @return a {@link Closeable} that allows the underlying web socket to be closed.
     */
    Closeable onOrderBookEvent(String symbol, Levels levels, ClientCallback<OrderBook> callback);

    /**
     * Open a new web socket to receive {@link DepthEvent depthEvents} on a callback.
     *
     * @param symbol   the market symbol to subscribe to
     * @param callback the callback to call on new events
     * @return a {@link Closeable} that allows the underlying web socket to be closed.
     */
    Closeable onDepthEvent(String symbol, ClientCallback<DepthEvent> callback);

    /**
     * Open a new web socket to receive {@link CandlestickEvent candlestickEvents} on a callback.
     *
     * @param symbol   the market symbol to subscribe to
     * @param interval the interval of the candles tick events required
     * @param callback the callback to call on new events
     * @return a {@link Closeable} that allows the underlying web socket to be closed.
     */
    Closeable onCandlestickEvent(String symbol, CandlestickInterval interval, ClientCallback<CandlestickEvent> callback);

    /**
     * Open a new web socket to receive {@link OrderTradeEvent tradeEvents} on a callback.
     *
     * @param symbol   the market symbol to subscribe to
     * @param callback the callback to call on new events
     * @return a {@link Closeable} that allows the underlying web socket to be closed.
     */
    Closeable onTradeEvent(String symbol, ClientCallback<OrderTradeEvent> callback);

    /**
     * Open a new web socket to receive {@link AllMarketTickersEvent allMarketTickersEvents} on a callback.
     *
     * @param symbol   the market symbol to subscribe to
     * @param callback the callback to call on new events
     * @return a {@link Closeable} that allows the underlying web socket to be closed.
     */
    Closeable onMarketTickerEvent(String symbol, ClientCallback<AllMarketTickersEvent> callback);

    /**
     * Open a new web socket to receive {@link UserDataUpdateEvent userDataUpdateEvents} on a callback.
     *
     * @param listenKey the listen key to subscribe to.
     * @param callback  the callback to call on new events
     * @return a {@link Closeable} that allows the underlying web socket to be closed.
     */
    Closeable onUserDataUpdateEvent(String listenKey, ClientCallback<UserDataUpdateEvent> callback);

    void close();
}
