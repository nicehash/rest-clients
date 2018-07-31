package com.nicehash.tools.marketmaker;

import com.nicehash.tools.marketmaker.event.EventType;
import com.nicehash.utils.cli.CliUtils;
import com.nicehash.utils.cli.Market;
import com.nicehash.exchange.client.ExchangeClient;
import com.nicehash.exchange.client.ExchangeClientFactory;
import com.nicehash.exchange.client.ExchangeWebSocketClient;
import com.nicehash.exchange.client.domain.account.Account;
import com.nicehash.exchange.client.domain.account.AssetBalance;
import com.nicehash.exchange.client.domain.event.AllMarketTickersEvent;
import com.nicehash.exchange.client.domain.event.CandlestickEvent;
import com.nicehash.exchange.client.domain.event.DepthEvent;
import com.nicehash.exchange.client.domain.event.Levels;
import com.nicehash.exchange.client.domain.event.OrderTradeEvent;
import com.nicehash.exchange.client.domain.market.CandlestickInterval;
import com.nicehash.exchange.client.domain.market.OrderBook;
import com.nicehash.exchange.client.domain.market.TickerPrice;
import com.nicehash.tools.marketmaker.event.XCandlestickEvent;
import com.nicehash.tools.marketmaker.event.XDepthEvent;
import com.nicehash.tools.marketmaker.event.XOrderEvent;
import com.nicehash.tools.marketmaker.event.XTickersEvent;
import com.nicehash.tools.marketmaker.event.XTimerEvent;
import com.nicehash.tools.marketmaker.event.XTradeEvent;
import com.nicehash.tools.marketmaker.strategy.RandomStrategy;
import com.nicehash.tools.marketmaker.strategy.Strategy;
import com.nicehash.external.ClientCallback;
import com.nicehash.external.spi.Options;
import com.nicehash.utils.options.OptionMap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private String endpointUrl = CliUtils.getNicehashUrl("https://api-test.nicehash.com/exchange");
    private String wsEndpointUrl = CliUtils.getNicehashWsUrl("https://exchange-test.nicehash.com/ws");

    private String key = CliUtils.getApiKey(null);
    private String secret = CliUtils.getApiSecret(null);

    private ScheduledExecutorService executor;
    private String lowPrice;
    private String highPrice;


    /**
     * Example usage:
     *
     *     nhmakerbot TLTCTBTC --gold-limit 50 --money-limit 5  --price 0.1 --tick 0.1
     *
     *  TODO: implement tick enforcement
     * @param args
     */
    public static void main(String [] args) {
        new Main().run(args);
    }


    public void run(String [] args) {

        if (args.length == 0) {
            printHelp();
            System.exit(0);
        }

        Config config = new Config();
        boolean exact = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch(arg) {
                case "--gold-limit": {
                    config.setGold(
                        new BigDecimal(CliUtils.ensureArgument(arg, args, ++i))
                    );
                    break;
                }
                case "--money-limit": {
                    config.setMoney(
                        new BigDecimal(CliUtils.ensureArgument(arg, args, ++i))
                    );
                    break;
                }
                case "--low-price": {
                    arg = CliUtils.ensureArgument(arg, args, ++i);
                    this.lowPrice = arg;
                    if (arg.startsWith("-") || arg.startsWith("+")) {
                        if (arg.endsWith("%")) {
                            config.setRelativeLowPricePct(parseRelativePricePct(arg));
                        } else {
                            config.setRelativeLowPrice(new BigDecimal(arg));
                        }
                    } else {
                        config.setLowPrice(new BigDecimal(arg));
                    }
                    break;
                }
                case "--high-price": {
                    arg = CliUtils.ensureArgument(arg, args, ++i);
                    this.highPrice = arg;
                    if (arg.startsWith("-") || arg.startsWith("+")) {
                        if (arg.endsWith("%")) {
                            config.setRelativeHighPricePct(parseRelativePricePct(arg));
                        } else {
                            config.setRelativeHighPrice(new BigDecimal(arg));
                        }
                    } else {
                        config.setHighPrice(new BigDecimal(arg));
                    }
                    break;
                }
                case "--price-pattern": {
                    config.setPricePattern(
                        CliUtils.ensureArgument(arg, args, ++i)
                    );
                    break;
                }
                case "--price": {
                    config.setPrice(
                        new BigDecimal(CliUtils.ensureArgument(arg, args, ++i))
                    );
                    break;
                }
                case "--ignore-limits": {
                    config.setIgnoreLimits(true);
                    break;
                }
                case "--actions-per-second": {
                    config.setActionsPerSecond(
                        Double.valueOf(CliUtils.ensureArgument(arg, args, ++i))
                    );
                    break;
                }
                case "--actions-limit": {
                    config.setActionsLimit(
                        Integer.valueOf(CliUtils.ensureArgument(arg, args, ++i))
                    );
                    break;
                }
                case "--tick": {
                    config.setTick(
                        new BigDecimal(CliUtils.ensureArgument("--tick", args, ++i))
                    );
                    break;
                }
                case "--events": {
                    processEventsArg(config, CliUtils.ensureArgument("--events", args, ++i));
                    break;
                }
                case "--type": {
                    config.setType(
                        CliUtils.ensureArgument("--type", args, ++i)
                    );
                    break;
                }
                case "--exact": {
                    exact = true;
                    break;
                }
                case "--no-take": {
                    config.setNoTake(true);
                    break;
                }
                default: {
                    if (config.getMarket() != null) {
                        throw new IllegalArgumentException("Unexpected argument: " + arg);
                    }
                    config.setMarket(new Market(arg));
                }
            }
        }

        if (exact) {
            if (lowPrice != null || highPrice != null) {
                throw new IllegalArgumentException("Option --exact can't be used in combination with --low-price or --high-price");
            }
            config.setRelativeLowPricePct(0);
            config.setRelativeHighPricePct(0);
        }

        if (config.isNoTake() && config.getType() != null) {
            throw new IllegalArgumentException("Option --no-take can't be used in combination with --type");
        }

        checkRequiredSettings(config);

        config.applyPrecisionLimitsIfNecessary(8, RoundingMode.HALF_UP);

        ExchangeClient client = defaultExchangeClient();
        Market market = config.getMarket();

        BigDecimal startPrice = config.getPrice();
        if (startPrice == null || startPrice.compareTo(BigDecimal.ZERO) <= 0) {

            TickerPrice currentPrice = null;
            try {
                currentPrice = client.getPrice(market.symbol());
            } catch (Exception e) {
                println("ERROR: Could not fetch current price: " + (e.getCause() != null ? e.getCause().toString() : e.toString()));
            }

            if (currentPrice != null && !"".equals(currentPrice.getPrice())) {
                startPrice = new BigDecimal(currentPrice.getPrice());
            } else {
                startPrice = null;
            }
        }

        if (startPrice == null || startPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Could not determine start price");
        }


        Account account = null;

        try {
            account = client.getAccount();
        } catch (Exception e) {
            error("Failed to get account info: ", e);
        }

        if (account != null) {
            List<AssetBalance> balances = account.getBalances();

            AssetBalance goldBalance = null, moneyBalance = null;

            println("Account: ");
            for (AssetBalance a : balances) {
                if (market.gold().equalsIgnoreCase(a.getAsset()) || market.money().equalsIgnoreCase(a.getAsset())) {
                    if (market.gold().equalsIgnoreCase(a.getAsset())) {
                        goldBalance = a;
                    } else {
                        moneyBalance = a;
                    }
                    println("  " + a.getAsset() + ": Available: " + a.getFree() + ", Reserved: " + a.getLocked());
                }
            }

            if (config.getGold().compareTo(goldBalance.getFree()) > 0) {
                throw new IllegalArgumentException("Trading limit for " + goldBalance.getAsset() +
                                                   " (" + config.getGold() + ") exceeds available amount (" + goldBalance.getFree() + ")");
            }

            if (config.getMoney().compareTo(moneyBalance.getFree()) > 0) {
                throw new IllegalArgumentException("Trading limit for " + moneyBalance.getAsset() +
                                                   " (" + config.getMoney() + ") exceeds available amount (" + moneyBalance.getFree() + ")");
            }

            if (config.getGold().compareTo(BigDecimal.ZERO) == 0) {
                config.setGold(goldBalance.getFree());
            }
            if (config.getMoney().compareTo(BigDecimal.ZERO) == 0) {
                config.setMoney(moneyBalance.getFree());
            }
        }

        printSummary(config, startPrice);


        // main event queue
        Queue queue = new ConcurrentLinkedQueue<>();

        // strategy to run
        Strategy strategy = new RandomStrategy(config, queue, client, startPrice);

        // register websocket listeners, and timer as event sources for the main queue
        linkEventSources(market, queue);

        // start event loop
        try {
            strategy.process();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted");
        } finally {
            executor.shutdown();
        }

        System.exit(0); // OkHttp holds non-daemon threads in the background
                               // TODO: Find how to shutdown OkHttp thread pool
    }

    private void printHelp() {
        println("Usage: nhmakerbot MARKET OPTIONS");
        println();
        println("Arguments:");
        println("  MARKET     Market symbol (e.g.: LTCBTC)");
        println();
        println("Options:");
        println("  --gold-limit QUANTITY         The quantity limit of base asset (e.g. LTC in LTCBTC market)");
        println("  --money-limit AMOUNT          The amount limit of quote asset (e.g. BTC in LTCBTC market)");
        println("  --price PRICE                 Target price around which to spread orders");
        println("  --low-price PRICE             Lower bound of targeted price range");
        println("  --high-price PRICE            Upper bound of targeted price range");
        println("  --price-pattern PATTERN       Price distribution to use when placing orders");
        println("  --actions-per-second NUM      Maximum number of orders that can be placed per second");
        println("  --actions-limit NUM           Create the specified number of orders and exit");
        println("  --ignore-limits               Don't take the decimal places limit into account (i.e. allow placing illegal orders)");
        println("  --tick PRICE_PATTERN          Amount to serve as a multiplier with which to align all order prices");
        println("  --type TYPE                   Limit orders to specific type. Possible values are: SELL, BUY");
        println("  --events EVENTS               Comma-separated list of event types to follow on stdout.");
        println("                                Possible values are: ORDER, TRADE, DEPTH, CANDLESTICK, TICKERS, or ALL");
    }

    private void processEventsArg(Config config, String value) {
        String [] events = value.split(",");
        HashSet<EventType> enabledEvents = new HashSet<>();
        for (String event: events) {
            if ("ALL".equals(event)) {
                for (EventType t: EventType.values()) {
                    enabledEvents.add(t);
                }
                break;
            }
            try {
                EventType et = EventType.valueOf(event);
                enabledEvents.add(et);
            } catch (Exception e) {
                throw new IllegalArgumentException("Unknown event type: " + event);
            }
        }
        config.setEnabledEvents(enabledEvents);
    }

    private void linkEventSources(Market market, Queue queue) {
        // Link up the queue with event sources
        ExchangeWebSocketClient wsClient = ExchangeClientFactory.newWebSocketClient(wsEndpointUrl);
        wsClient.onDepthEvent(market.symbol(), new ClientCallback<DepthEvent>() {
            @Override
            public void onResponse(DepthEvent result) {
                queue.add(new XDepthEvent(result));
            }

            @Override
            public void onFailure(Throwable t) {
                error("onDepthEvent FAILED: ", t);
            }
        });

        wsClient.onMarketTickerEvent(market.symbol(), new ClientCallback<AllMarketTickersEvent>() {
            @Override
            public void onResponse(AllMarketTickersEvent result) {
                queue.add(new XTickersEvent(result));
            }

            @Override
            public void onFailure(Throwable t) {
                error("onMarketTickerEvent FAILED: ", t);
            }
        });

        wsClient.onOrderBookEvent(market.symbol(), Levels.L5, new ClientCallback<OrderBook>() {
            @Override
            public void onResponse(OrderBook result) {
                queue.add(new XOrderEvent(result));
            }

            @Override
            public void onFailure(Throwable t) {
                error("onOrderBookEvent FAILED: ", t);
            }
        });

        wsClient.onCandlestickEvent(market.symbol(), CandlestickInterval.ONE_MINUTE, new ClientCallback<CandlestickEvent>() {
            @Override
            public void onResponse(CandlestickEvent result) {
                queue.add(new XCandlestickEvent(result));
            }

            @Override
            public void onFailure(Throwable t) {
                error("onCandlestickEvent FAILED: ", t);
            }
        });

        wsClient.onTradeEvent(market.symbol(), new ClientCallback<OrderTradeEvent>() {
            @Override
            public void onResponse(OrderTradeEvent result) {
                queue.add(new XTradeEvent(result));
            }

            @Override
            public void onFailure(Throwable t) {
                error("onTradeEvent FAILED:", t);
            }
        });

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> queue.add(new XTimerEvent()), 1, 1, TimeUnit.SECONDS);
    }

    private void printSummary(Config config, BigDecimal startPrice) {
        Market market = config.getMarket();
        println("Target market: " + market.symbol());
        println("Trading limits: " + config.getGold().toPlainString() + " " + market.gold());
        println("                  " + config.getMoney().toPlainString() + " " + market.money());
        println("Target price: " + startPrice);
        println("Low Price: " + (lowPrice != null ? lowPrice :
                "-" + new BigDecimal(config.getRelativeLowPricePct() * 100).setScale(0).toPlainString() + "%"));
        println("High Price: " + (highPrice != null ? highPrice :
                "+" + new BigDecimal(config.getRelativeHighPricePct() * 100).setScale(0).toPlainString() + "%"));

        if (config.getPricePattern() != null) {
            println("Price pattern: " + config.getPricePattern());
        }
    }

    public static void println() {
        System.out.println();
    }

    public static void println(String text) {
        System.out.println(text);
    }

    public static void error(String s, Throwable t) {
        println(s);
        t.printStackTrace();
    }

    private ExchangeClient defaultExchangeClient() {
        OptionMap.Builder builder = defaultOptionBuilder();

        ExchangeClientFactory factory = ExchangeClientFactory.newInstance(builder.getMap());
        return factory.newClient();
    }

    private OptionMap.Builder defaultOptionBuilder() {
        OptionMap.Builder builder = OptionMap.builder()
            .set(Options.BASE_URL, endpointUrl)
            .set(Options.WS_BASE_URL, wsEndpointUrl)
            .set(Options.KEY, key)
            .set(Options.SECRET, secret)
            .set(Options.READ_TIMEOUT, 60000);

        return builder;
    }

    private void checkRequiredSettings(Config config) {
        if (key == null) {
            throw new RuntimeException("API KEY not set");
        }

        if (secret == null) {
            throw new RuntimeException("API SECRET not set");
        }

        if (config.getMarket() == null) {
            throw new RuntimeException("No market specified");
        }
    }

    private double parseRelativePricePct(String arg) {
        StringBuilder sb = new StringBuilder();
        sb.append(arg.charAt(0));
        for (int j = 1; j < arg.length(); j++) {
            char c = arg.charAt(j);
            if (c == '.' || Character.isDigit(c)) {
                sb.append(c);
            } else {
                break;
            }
        }
        return Double.valueOf(sb.toString());
    }
}
