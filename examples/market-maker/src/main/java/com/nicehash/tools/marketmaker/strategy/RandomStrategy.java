package com.nicehash.tools.marketmaker.strategy;

import com.nicehash.exchange.client.ExchangeClient;
import com.nicehash.exchange.client.domain.OrderSide;
import com.nicehash.exchange.client.domain.OrderType;
import com.nicehash.exchange.client.domain.TimeInForce;
import com.nicehash.exchange.client.domain.account.NewOrder;
import com.nicehash.exchange.client.domain.account.NewOrderResponse;
import com.nicehash.exchange.client.domain.event.AllMarketTickersEvent;
import com.nicehash.exchange.client.domain.event.CandlestickEvent;
import com.nicehash.exchange.client.domain.event.DepthEvent;
import com.nicehash.exchange.client.domain.event.OrderTradeEvent;
import com.nicehash.exchange.client.domain.market.OrderBook;
import com.nicehash.tools.marketmaker.Config;
import com.nicehash.tools.marketmaker.event.Event;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Queue;
import java.util.Random;

public class RandomStrategy implements Strategy {

    private Random random = new Random();

    private final Config config;
    private final Queue<Event> queue;
    private final ExchangeClient client;

    /**
     *
     * @param config Configuration
     * @param queue Queue with incoming events the strategy uses as input for independent trading actions
     */
    public RandomStrategy(Config config, Queue<Event> queue, ExchangeClient client) {
        this.config = config;
        this.queue = queue;
        this.client = client;
    }

    /**
     *  Wait for some time or
     */
    public void process() throws InterruptedException {
        // wait for events like price change, new order inserted, new trade executed ...
        // you also get a timer event with some resolution

        while (true) {
            Event event = queue.poll();

            if (event == null) {
                Thread.sleep(100);
            } else {
                processEvent(event);
            }
        }
    }

    private void processEvent(Event event) {
        switch (event.type()) {
            case TICKERS: {
                AllMarketTickersEvent e = AllMarketTickersEvent.class.cast(event.getApiEvent());
                System.out.println("Event[TICKERS]: " + e);
                break;
            }
            case DEPTH: {
                DepthEvent e = DepthEvent.class.cast(event.getApiEvent());
                System.out.println("Event[DEPTH]: Asks (LIMIT SELLs): " + e.getAsks() +
                                 "\n              Bids (LIMIT BUYs): " + e.getBids());
                break;
            }
            case TRADE: {
                OrderTradeEvent e = OrderTradeEvent.class.cast(event.getApiEvent());
                System.out.println("Event[TRADE]: " + e);
                break;
            }
            case ORDER: {
                OrderBook e = OrderBook.class.cast(event.getApiEvent());
                System.out.println("Event[ORDER]: " + e);
                break;
            }
            case CANDLESTICK: {
                CandlestickEvent e = CandlestickEvent.class.cast(event.getApiEvent());
                System.out.println("Event[CANDLESTICK]: " + e);
                break;
            }
            case TIMER: {
                NewOrder newOrder = new NewOrder(config.getMarket().symbol(), randomOrderSide(), OrderType.LIMIT, TimeInForce.GTC, randomQuantity(), randomPrice());
                System.out.println("New order: " + newOrder);
                NewOrderResponse response = client.newOrder(newOrder);
                System.out.println("Created new order: " + response);
                break;
            }
            default:
                throw new RuntimeException("Unknown event: " + event.type() + " [" + event + "]");
        }
    }

    private BigDecimal randomPrice() {
        BigDecimal p = new BigDecimal(random.nextDouble() * 0.5);
        if (!config.isIgnoreLimits()) {
            p = p.setScale(8, RoundingMode.HALF_UP);
        }
        return p;
    }

    private BigDecimal randomQuantity() {
        BigDecimal q = new BigDecimal(random.nextDouble() + 0.5);
        if (!config.isIgnoreLimits()) {
            q = q.setScale(8, RoundingMode.HALF_UP);
        }
        return q;
    }

    private OrderSide randomOrderSide() {
        return random.nextBoolean() ? OrderSide.BUY : OrderSide.SELL;
    }
}
