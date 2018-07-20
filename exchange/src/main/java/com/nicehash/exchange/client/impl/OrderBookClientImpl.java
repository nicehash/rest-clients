package com.nicehash.exchange.client.impl;

import com.nicehash.exchange.client.ExchangeClient;
import com.nicehash.exchange.client.ExchangeClientFactory;
import com.nicehash.exchange.client.ExchangeWebSocketClient;
import com.nicehash.exchange.client.OrderBookClient;
import com.nicehash.exchange.client.domain.event.DepthEvent;
import com.nicehash.exchange.client.domain.market.OrderBook;
import com.nicehash.exchange.client.domain.market.OrderBookEntry;
import com.nicehash.exchange.client.domain.market.OrderBookSync;
import com.nicehash.exchange.client.domain.market.SyncHandle;
import com.nicehash.external.ClientCallback;
import com.nicehash.utils.options.OptionMap;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Ales Justin
 */
public class OrderBookClientImpl implements OrderBookClient {
    private final ExchangeClient restClient;
    private final ExchangeWebSocketClient wsClient;

    public OrderBookClientImpl(OptionMap options) {
        ExchangeClientFactory factory = ExchangeClientFactory.newInstance(options);
        this.restClient = factory.newClient();
        wsClient = ExchangeClientFactory.newWebSocketClient(options);
    }

    private void applyDiff(List<OrderBookEntry> list, Map<BigDecimal, BigDecimal> map) {
        for (OrderBookEntry entry : list) {
            BigDecimal price = entry.getPrice();
            BigDecimal qty = entry.getQty();
            if (qty.signum() == 0) {
                map.remove(price);
            } else {
                map.put(price, qty);
            }
        }
    }

    @Override
    public OrderBookSync orderBook(String symbol) {
        BlockingDeque<DepthEvent> events = new LinkedBlockingDeque<>();

        Closeable closeable = wsClient.onDepthEvent(symbol, new ClientCallback<DepthEvent>() {
            @Override
            public void onResponse(DepthEvent event) {
                try {
                    events.putLast(event);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });

        OBCloseable obc = new OBCloseable(closeable);
        SyncHandle handle = new SyncHandle(obc);

        Map<BigDecimal, BigDecimal> bids = new TreeMap<>(Comparator.reverseOrder());
        Map<BigDecimal, BigDecimal> asks = new TreeMap<>(Comparator.naturalOrder());
        OrderBookSync orderBook = new OrderBookSync(handle, bids, asks);

        OrderBook ob = restClient.getOrderBook(symbol, null);
        applyDiff(ob.getBids(), bids);
        applyDiff(ob.getAsks(), asks);
        long lastUpdateId = ob.getLastUpdateId();
        orderBook.setLastUpdateId(lastUpdateId);

        new Thread(() -> {
            try {
                while (obc.stopped == false) {
                    DepthEvent event = events.takeFirst();
                    long finalUpdateId = event.getFinalUpdateId();
                    if (finalUpdateId > lastUpdateId) {
                        handle.writeLock().lock();
                        try {
                            applyDiff(event.getBids(), bids);
                            applyDiff(event.getAsks(), asks);
                        } finally {
                            handle.writeLock().unlock();
                        }
                        orderBook.setLastUpdateId(finalUpdateId);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }, "OrderBookSyncThread-" + symbol).start();

        return orderBook;
    }

    @Override
    public void close() throws IOException {
        try {
            wsClient.close();
        } finally {
            restClient.close();
        }
    }

    private static class OBCloseable implements Closeable {
        private final Closeable wsCloseable;
        private volatile boolean stopped = false;

        public OBCloseable(Closeable wsCloseable) {
            this.wsCloseable = wsCloseable;
        }

        @Override
        public void close() throws IOException {
            stopped = true;
            wsCloseable.close();
        }
    }

}
