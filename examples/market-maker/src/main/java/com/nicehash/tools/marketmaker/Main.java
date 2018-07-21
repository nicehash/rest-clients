package com.nicehash.tools.marketmaker;

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
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    String endpointUrl = CliUtils.getNicehashUrl("https://api-test.nicehash.com/exchange");
    String wsEndpointUrl = CliUtils.getNicehashWsUrl("https://exchange-test.nicehash.com/ws");

    String key = CliUtils.getApiKey(null);
    String secret = CliUtils.getApiSecret(null);

    /**
     * Example usage:
     *
     *     nhmakerbot TLTCTBTC --gold-quantity 50 --money-quantity 5 --spread 0.1 --buckets 10 --fallback-price 0.1 --tick 0.1
     *
     * @param args
     */
    public static void main(String [] args) {
        new Main().run(args);
    }


    public void run(String [] args) {

        Config config = new Config();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch(arg) {
                case "--gold-quantity": {
                    config.setGold(
                        new BigDecimal(CliUtils.ensureArgument("--gold-quantity", args, ++i))
                    );
                    break;
                }
                case "--money-quantity": {
                    config.setMoney(
                        new BigDecimal(CliUtils.ensureArgument("--money-quantity", args, ++i))
                    );
                    break;
                }
                case "--spread": {
                    config.setSpread(
                        Double.valueOf(CliUtils.ensureArgument("--spread", args, ++i))
                    );
                    break;
                }
                case "--buckets": {
                    config.setBuckets(
                        Integer.valueOf(CliUtils.ensureArgument("--buckets", args, ++i))
                    );
                    break;
                }
                case "--price-override": {
                    config.setPriceOverride(
                        new BigDecimal(CliUtils.ensureArgument("--price-override", args, ++i))
                    );
                    break;
                }
                case "--fallback-price": {
                    config.setFallbackPrice(
                        new BigDecimal(CliUtils.ensureArgument("--fallback-price", args, ++i))
                    );
                    break;
                }
                case "--ignore-limits": {
                    config.setIgnoreLimits(true);
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

        checkRequiredSettings(config);
        if (!config.isIgnoreLimits()) {
            config.applyPrecisionLimits(8, RoundingMode.HALF_UP);
        }

        ExchangeClient client = defaultExchangeClient();
        Account account = client.getAccount();

        printSummary(config);


        List<AssetBalance> balances = account.getBalances();

        AssetBalance goldBalance = null, moneyBalance = null;
        Market market = config.getMarket();

        println("Account: ");
        for (AssetBalance a: balances) {
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

        BigDecimal startPrice = config.getPriceOverride();
        if (config.getPriceOverride() == null || config.getPriceOverride().compareTo(BigDecimal.ZERO) <= 0) {

            TickerPrice currentPrice = null;
            try {
                currentPrice = client.getPrice(market.symbol());
            } catch (Exception e) {
                println("ERROR: Could not fetch current price: " + e.getCause().toString());
            }

            if (currentPrice != null && "".equals(currentPrice.getPrice())) {
                currentPrice = null;
            }

            if (currentPrice == null) {
                println("WARN: No current price for market");
                if (config.getFallbackPrice().compareTo(BigDecimal.ZERO) > 0) {
                    println("Using fallback price: " + config.getFallbackPrice().toPlainString());
                    startPrice = config.getFallbackPrice();
                }
            } else {
                startPrice = new BigDecimal(currentPrice.getPrice());
            }
        }

        if (startPrice == null || startPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Could not determine start price");
        }
        println("Start price: " + startPrice.toPlainString());

        // main event queue
        Queue queue = new ConcurrentLinkedQueue<>();

        linkEventSources(market, queue);


        Strategy strategy = new RandomStrategy(config, queue, client);
        try {
            strategy.process();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted");
        }
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

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> queue.add(new XTimerEvent()), 1, 5, TimeUnit.SECONDS);
    }

    private void printSummary(Config config) {
        Market market = config.getMarket();
        println("Target market: " + market.symbol());
        println("Trading limits: " + config.getGold().toPlainString() + " " + market.gold());
        println("                  " + config.getMoney().toPlainString() + " " + market.money());
        println("Initial price override: " + config.getPriceOverride().toPlainString());
        println("Spread: " + config.getSpread());
        println("Buckets: " + config.getBuckets());
    }

    private static void println(String text) {
        System.out.println(text);
    }

    private static void error(String s, Throwable t) {
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
}
