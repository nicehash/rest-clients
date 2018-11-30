package com.nicehash.clients.exchange.impl;

import com.nicehash.clients.exchange.ExchangeWebSocketClient;
import com.nicehash.clients.exchange.domain.event.AllMarketTickersEvent;
import com.nicehash.clients.exchange.domain.event.CandlestickEvent;
import com.nicehash.clients.exchange.domain.event.DepthEvent;
import com.nicehash.clients.exchange.domain.event.Levels;
import com.nicehash.clients.exchange.domain.event.OrderTradeEvent;
import com.nicehash.clients.exchange.domain.event.UserDataUpdateEvent;
import com.nicehash.clients.exchange.domain.market.CandlestickInterval;
import com.nicehash.clients.exchange.domain.market.OrderBook;
import com.nicehash.clients.common.ClientCallback;
import com.nicehash.clients.common.spi.Options;
import com.nicehash.clients.util.options.OptionMap;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import java.io.Closeable;

/**
 * Exchange API WebSocket client implementation using OkHttp.
 */
public class ExchangeWebSocketClientImpl implements ExchangeWebSocketClient, Closeable {

    private final OkHttpClient client;
    private final String baseUrl;

    public ExchangeWebSocketClientImpl(OptionMap options) {
        String url = options.get(Options.WS_BASE_URL);
        if (url == null) {
            url = options.get(Options.BASE_URL);
        }
        if (url == null) {
            throw new IllegalArgumentException("Missing base url!");
        }
        this.baseUrl = url;

        OkHttpClient client = options.get(Options.WEBSOCKET_CLIENT);
        if (client == null) {
            Dispatcher dispatcher = options.get(Options.DISPATCHER);
            if (dispatcher == null) {
                dispatcher = new Dispatcher();
                dispatcher.setMaxRequestsPerHost(100);
            }
            client = new OkHttpClient.Builder().dispatcher(dispatcher).build();
        }
        this.client = client;
    }

    @Override
    public Closeable onOrderBookEvent(String symbol, Levels levels, ClientCallback<OrderBook> callback) {
        final String channel = String.format("%s@depth%s", symbol, levels.get());
        return createNewWebSocket(channel, new ExchangeWebSocketListener<>(callback, OrderBook.class));
    }

    public Closeable onDepthEvent(String symbol, ClientCallback<DepthEvent> callback) {
        final String channel = String.format("%s@depth", symbol);
        return createNewWebSocket(channel, new ExchangeWebSocketListener<>(callback, DepthEvent.class));
    }

    @Override
    public Closeable onCandlestickEvent(String symbol, CandlestickInterval interval, ClientCallback<CandlestickEvent> callback) {
        final String channel = String.format("%s@kline_%s", symbol, interval.getIntervalId());
        return createNewWebSocket(channel, new ExchangeWebSocketListener<>(callback, CandlestickEvent.class));
    }

    public Closeable onTradeEvent(String symbol, ClientCallback<OrderTradeEvent> callback) {
        final String channel = String.format("%s@trade", symbol);
        return createNewWebSocket(channel, new ExchangeWebSocketListener<>(callback, OrderTradeEvent.class));
    }

    public Closeable onMarketTickerEvent(String symbol, ClientCallback<AllMarketTickersEvent> callback) {
        final String channel = String.format("%s@ticker", symbol);
        return createNewWebSocket(channel, new ExchangeWebSocketListener<>(callback, AllMarketTickersEvent.class));
    }

    public Closeable onUserDataUpdateEvent(String listenKey, ClientCallback<UserDataUpdateEvent> callback) {
        return createNewWebSocket(listenKey, new ExchangeWebSocketListener<>(callback, UserDataUpdateEvent.class));
    }

    @Override
    public void close() {
        client.dispatcher().executorService().shutdown();
    }

    private Closeable createNewWebSocket(String channel, ExchangeWebSocketListener<?> listener) {
        String streamingUrl = String.format("%s/%s", baseUrl, channel);
        Request request = new Request.Builder().url(streamingUrl).build();
        final WebSocket webSocket = client.newWebSocket(request, listener);
        return () -> {
            final int code = 1000;
            listener.onClosing(webSocket, code, null);
            webSocket.close(code, null);
            listener.onClosed(webSocket, code, null);
        };
    }
}
