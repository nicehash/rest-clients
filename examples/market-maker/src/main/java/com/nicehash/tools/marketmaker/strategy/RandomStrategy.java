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
import com.nicehash.exchange.client.domain.market.OrderBookEntry;
import com.nicehash.tools.marketmaker.Config;
import com.nicehash.tools.marketmaker.event.Event;
import com.nicehash.tools.marketmaker.event.EventType;
import com.nicehash.tools.marketmaker.event.XTimerEvent;

import static com.nicehash.tools.marketmaker.Main.error;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class RandomStrategy implements Strategy {

    private Random random = new Random();

    private final Config config;
    private final Queue<Event> queue;
    private final ExchangeClient client;

    private long timeIntervalMs;
    private long lastActionTime = 0;

    private BigDecimal targetPrice;
    private BigDecimal lowPrice;
    private BigDecimal highPrice;

    private BigDecimal gold;
    private BigDecimal goldFreeze = BigDecimal.ZERO;

    private BigDecimal money;
    private BigDecimal moneyFreeze = BigDecimal.ZERO;

    private ConcurrentHashMap<String, PlacedOrder> orders = new ConcurrentHashMap<>();

    private boolean mustExit = false;
    private int actionCount = 0;

    /**
     * @param config Configuration
     * @param queue Queue with incoming events the strategy uses as input for independent trading actions
     * @param client ExchangeClient instance for using REST API through methods
     * @param targetPrice Target price (current market price or explicitly set price)
     */
    public RandomStrategy(Config config, Queue<Event> queue, ExchangeClient client, BigDecimal targetPrice) {
        this.config = config;
        this.timeIntervalMs = (long) (1 / (double) config.getActionsPerSecond() * 1000);

        this.targetPrice = targetPrice;
        this.queue = queue;
        this.client = client;
        lowPrice = calculateLowPrice();
        highPrice = calculateHighPrice();

        if (!config.isIgnoreLimits()) {
            lowPrice = lowPrice.setScale(8, RoundingMode.HALF_UP);
            highPrice = highPrice.setScale(8, RoundingMode.HALF_UP);
        }

        if (lowPrice.compareTo(highPrice) > 0) {
            throw new RuntimeException("High price should be higher than or equal to low price");
        }

        gold = config.getGold();
        money = config.getMoney();
    }

    /**
     *  Entry point into Strategy class
     */
    public void process() throws InterruptedException {
        // wait for events like price change, new order inserted, new trade executed ...
        // you also get a timer event with some resolution

        while (!mustExit) {
            Event event = queue.poll();

            if (event == null) {
                Thread.sleep(10);
            } else if (event.type() == EventType.TIMER &&
                   System.currentTimeMillis() < XTimerEvent.class.cast(event).getApiEvent()) {
                Thread.sleep(10);
                queue.add(event);
            } else {
                processEvent(event);
            }
        }
    }

    private void processEvent(Event event) {
        try {
            switch (event.type()) {
                case TICKERS: {
                    AllMarketTickersEvent e = AllMarketTickersEvent.class.cast(event.getApiEvent());
                    if (config.isEnabledEvent(EventType.TICKERS)) {
                        System.out.println("Event[TICKERS]: " + e);
                    }
                    break;
                }
                case DEPTH: {
                    DepthEvent e = DepthEvent.class.cast(event.getApiEvent());
                    if (config.isEnabledEvent(EventType.DEPTH)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Event[DEPTH]: eventTime: " + e.getEventTime() + " (" + formatTime(e.getEventTime()) + ")\n");
                        sb.append("  Bids: \n");
                        List<OrderBookEntry> bids = e.getBids();
                        for (OrderBookEntry bid: bids) {
                            sb.append("        " + bid + "\n");
                        }
                        sb.append("  Asks: \n");
                        List<OrderBookEntry> asks = e.getAsks();
                        for (OrderBookEntry ask: asks) {
                            sb.append("        " + ask + "\n");
                        }
                        System.out.println(sb);
                    }
                    break;
                }
                case TRADE: {
                    OrderTradeEvent e = OrderTradeEvent.class.cast(event.getApiEvent());
                    if (config.isEnabledEvent(EventType.TRADE)) {
                        System.out.println("Event[TRADE]: " + e);
                    }
                    break;
                }
                case ORDER: {
                    OrderBook e = OrderBook.class.cast(event.getApiEvent());
                    if (config.isEnabledEvent(EventType.ORDER)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Event[ORDER]: lastUpdateId: " + e.getLastUpdateId() + "\n");
                        sb.append("  Bids: \n");
                        List<OrderBookEntry> bids = e.getBids();
                        for (OrderBookEntry bid: bids) {
                            sb.append("        " + bid + "\n");
                        }
                        sb.append("  Asks: \n");
                        List<OrderBookEntry> asks = e.getAsks();
                        for (OrderBookEntry ask: asks) {
                            sb.append("        " + ask + "\n");
                        }
                        System.out.println(sb);
                    }
                    break;
                }
                case CANDLESTICK: {
                    CandlestickEvent e = CandlestickEvent.class.cast(event.getApiEvent());
                    if (config.isEnabledEvent(EventType.CANDLESTICK)) {
                        System.out.println("Event[CANDLESTICK]: " + e);
                    }
                    break;
                }
                case TIMER: {
                    if (config.getActionsLimit() > 0 && actionCount >= config.getActionsLimit()) {
                        mustExit = true;
                        break;
                    }
                    long start = System.currentTimeMillis();

                    long nextDueTime = lastActionTime + timeIntervalMs;
                    if (nextDueTime > start) {
                        reschedule(nextDueTime);
                        return;
                    }

                    lastActionTime = start;
                    performAction();
                    actionCount += 1;
                    break;
                }
                default:
                    throw new RuntimeException("Unknown event: " + event.type() + " [" + event + "]");
            }
        } catch (Exception e) {
            error("Error processing event " + event.type(), e);
        }
    }

    private void reschedule(long nextDueTime) {
        // add another Timer event into the queue
        queue.add(new XTimerEvent(nextDueTime));
    }

    private String formatTime(long time) {
        LocalDateTime datetime = LocalDateTime.ofEpochSecond((long) (time / 1000d), (int) (time % 1000) * 1000000, ZoneOffset.UTC);
        return datetime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    private void performAction() {
        BigDecimal randomPrice = randomPrice();
        NewOrder newOrder = new NewOrder(
            config.getMarket().symbol(),
            config.getType() != null ? config.getType() : config.isNoTake() ? noTakeOrderSide(randomPrice) : randomOrderSide(),
            OrderType.LIMIT,
            TimeInForce.GTC,
            randomQuantity(),
            randomPrice);
        System.out.println("New order: " + newOrder);

        NewOrderResponse response = client.newOrder(newOrder);
        System.out.println("Created new order: " + response);
        orders.put(response.getOrderId().toString(), new PlacedOrder(newOrder, response));

        if (newOrder.getSide() == OrderSide.BUY) {
            BigDecimal reserve = newOrder.getPrice().multiply(newOrder.getQuantity());
            if (money.compareTo(reserve) < 0) {
                throw new RuntimeException("Reached money limit - required funds: " + reserve.toPlainString() + ", available: " + money.toPlainString());
            } else {
                money.subtract(reserve);
                moneyFreeze.add(reserve);
            }
        } else {
            BigDecimal reserve = newOrder.getQuantity();
            if (gold.compareTo(reserve) < 0) {
                throw new RuntimeException("Reached gold limit - required amount: " + reserve.toPlainString() + ", available: " + gold.toPlainString());
            } else {
                gold.subtract(reserve);
                goldFreeze.add(reserve);
            }
        }
    }

    private OrderSide noTakeOrderSide(BigDecimal randomPrice) {
        return randomPrice.compareTo(targetPrice) > 0 ? OrderSide.SELL : OrderSide.BUY;
    }

    private BigDecimal randomPrice() {

        // use price pattern as probability distribution
        // spread over price interval

        // pick value
        int block = random.nextInt(config.getPricePatternBlockCount());

        // pick price within block
        double blockSize = getBlockSize(lowPrice, highPrice, config.getPricePatternArray().length);
        double blockLowPrice = getBlockLowPrice(block, lowPrice, highPrice, blockSize);
        double price = blockLowPrice + random.nextDouble() * blockSize;

        BigDecimal p = new BigDecimal(price);

        p = config.adjustForTick(p);
        p = applyScaleAsNecessary(p);

        if (p.equals(BigDecimal.ZERO)) {
            throw new RuntimeException("Tick rounding resulted in zero price");
        }
        return p;
    }

    private double getBlockSize(BigDecimal lowPrice, BigDecimal highPrice, int pricePatternBlockCount) {
        return (highPrice.doubleValue() - lowPrice.doubleValue()) / pricePatternBlockCount;
    }

    private double getBlockLowPrice(int block, BigDecimal lowPrice, BigDecimal highPrice, double blockSize) {

        int [] pattern = config.getPricePatternArray();
        int count = 0;
        for (int i = 0; i < pattern.length; i++) {
            count += pattern[i];
            if (count > block) {
                return lowPrice.doubleValue() + (blockSize * i);
            }
        }
        return highPrice.doubleValue() - blockSize;
    }

    private BigDecimal calculateLowPrice() {
        BigDecimal lowPrice = config.getLowPrice();
        if (lowPrice == null) {
            BigDecimal relLowPrice = config.getRelativeLowPrice();
            if (relLowPrice == null) {
                double relLowPricePct = config.getRelativeLowPricePct();
                lowPrice = targetPrice.multiply(new BigDecimal(1 + relLowPricePct));
            } else {
                lowPrice = targetPrice.add(relLowPrice);
            }
        }
        return lowPrice;
    }

    private BigDecimal calculateHighPrice() {
        BigDecimal highPrice = config.getHighPrice();
        if (highPrice == null) {
            BigDecimal relHighPrice = config.getRelativeHighPrice();
            if (relHighPrice == null) {
                double relHighPricePct = config.getRelativeHighPricePct();
                highPrice = targetPrice.multiply(new BigDecimal(1 + relHighPricePct));
            } else {
                highPrice = targetPrice.add(relHighPrice);
            }
        }
        return highPrice;
    }

    private BigDecimal randomQuantity() {
        // let order quantity be between 0.5, and 1.5
        BigDecimal q = new BigDecimal(random.nextDouble() + 0.5);
        return applyScaleAsNecessary(q);
    }

    private OrderSide randomOrderSide() {
        return random.nextBoolean() ? OrderSide.BUY : OrderSide.SELL;
    }

    private BigDecimal applyScaleAsNecessary(BigDecimal p) {
        if (!config.isIgnoreLimits()) {
            p = p.setScale(8, RoundingMode.HALF_UP);
        }
        return p;
    }

    static class PlacedOrder {
        final NewOrder order;
        final NewOrderResponse response;

        PlacedOrder(NewOrder order, NewOrderResponse response) {
            this.order = order;
            this.response = response;
        }
    }
}
