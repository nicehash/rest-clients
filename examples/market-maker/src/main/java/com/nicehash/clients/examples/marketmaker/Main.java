package com.nicehash.clients.examples.marketmaker;

import com.nicehash.clients.common.ClientCallback;
import com.nicehash.clients.common.spi.Options;
import com.nicehash.clients.examples.marketmaker.event.EventType;
import com.nicehash.clients.examples.marketmaker.event.XCandlestickEvent;
import com.nicehash.clients.examples.marketmaker.event.XDepthEvent;
import com.nicehash.clients.examples.marketmaker.event.XOrderEvent;
import com.nicehash.clients.examples.marketmaker.event.XTickersEvent;
import com.nicehash.clients.examples.marketmaker.event.XTimerEvent;
import com.nicehash.clients.examples.marketmaker.event.XTradeEvent;
import com.nicehash.clients.examples.marketmaker.strategy.RandomStrategy;
import com.nicehash.clients.examples.marketmaker.strategy.Strategy;
import com.nicehash.clients.exchange.ExchangeClient;
import com.nicehash.clients.exchange.ExchangeClientFactory;
import com.nicehash.clients.exchange.ExchangeWebSocketClient;
import com.nicehash.clients.exchange.domain.account.Account;
import com.nicehash.clients.exchange.domain.account.AssetBalance;
import com.nicehash.clients.exchange.domain.event.AllMarketTickersEvent;
import com.nicehash.clients.exchange.domain.event.CandlestickEvent;
import com.nicehash.clients.exchange.domain.event.DepthEvent;
import com.nicehash.clients.exchange.domain.event.Levels;
import com.nicehash.clients.exchange.domain.event.OrderTradeEvent;
import com.nicehash.clients.exchange.domain.market.CandlestickInterval;
import com.nicehash.clients.exchange.domain.market.OrderBook;
import com.nicehash.clients.exchange.domain.market.TickerPrice;
import com.nicehash.clients.util.cli.CliUtils;
import com.nicehash.clients.util.cli.Market;
import com.nicehash.clients.util.options.OptionMap;
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

  protected static final String ENDPOINT_URL =
      CliUtils.getNicehashUrl("https://api-test.nicehash.com/exchange");
  protected static final String WS_ENDPOINT_URL =
      CliUtils.getNicehashWsUrl("https://exchange-test.nicehash.com/ws");

  protected static final String API_KEY = CliUtils.getApiKey(null);
  protected static final String API_SECRET = CliUtils.getApiSecret(null);

  private ScheduledExecutorService executor;
  private String lowPrice;
  private String highPrice;

  /**
   * Example usage:
   *
   * <p>nhmakerbot TLTCTBTC --gold-limit 50 --money-limit 5 --price 0.1 --tick 0.1
   *
   * <p>TODO: implement tick enforcement
   *
   * @param args
   */
  public static void main(String[] args) {
    new Main().run(args);
  }

  public void run(String[] args) {

    if (args.length == 0) {
      printHelp();
      System.exit(0);
    }

    Config config = new Config();
    boolean exact = false;

    for (int i = 0; i < args.length; i++) {
      String arg = args[i];

      switch (arg) {
        case "--gold-limit":
          {
            config.setGold(new BigDecimal(CliUtils.ensureArgument(arg, args, ++i)));
            break;
          }
        case "--money-limit":
          {
            config.setMoney(new BigDecimal(CliUtils.ensureArgument(arg, args, ++i)));
            break;
          }
        case "--low-price":
          {
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
        case "--high-price":
          {
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
        case "--price-pattern":
          {
            config.setPricePattern(CliUtils.ensureArgument(arg, args, ++i));
            break;
          }
        case "--price":
          {
            config.setPrice(new BigDecimal(CliUtils.ensureArgument(arg, args, ++i)));
            break;
          }
        case "--ignore-limits":
          {
            config.setIgnoreLimits(true);
            break;
          }
        case "--actions-per-second":
          {
            config.setActionsPerSecond(Double.valueOf(CliUtils.ensureArgument(arg, args, ++i)));
            break;
          }
        case "--actions-limit":
          {
            config.setActionsLimit(Integer.valueOf(CliUtils.ensureArgument(arg, args, ++i)));
            break;
          }
        case "--tick":
          {
            config.setTick(new BigDecimal(CliUtils.ensureArgument("--tick", args, ++i)));
            break;
          }
        case "--events":
          {
            processEventsArg(config, CliUtils.ensureArgument("--events", args, ++i));
            break;
          }
        case "--type":
          {
            config.setType(CliUtils.ensureArgument("--type", args, ++i));
            break;
          }
        case "--exact":
          {
            exact = true;
            break;
          }
        case "--no-take":
          {
            config.setNoTake(true);
            break;
          }
        case "--cancel-on-limit":
          {
            config.setCancelOnLimit(true);
            break;
          }
        default:
          {
            if (config.getMarket() != null) {
              throw new IllegalArgumentException("Unexpected argument: " + arg);
            }
            config.setMarket(new Market(arg));
          }
      }
    }

    if (exact) {
      if (lowPrice != null || highPrice != null) {
        throw new IllegalArgumentException(
            "Option --exact can't be used in combination with --low-price or --high-price");
      }
      config.setRelativeLowPricePct(0);
      config.setRelativeHighPricePct(0);
    }

    if (config.isNoTake() && config.getType() != null) {
      throw new IllegalArgumentException(
          "Option --no-take can't be used in combination with --type");
    }

    checkRequiredSettings(config);

    config.applyPrecisionLimitsIfNecessary(8, RoundingMode.HALF_UP);

    ExchangeClient client = defaultExchangeClient();
    Market market = config.getMarket();
    println("Target market: " + market.symbol());

    TickerPrice currentPrice = null;
    try {
      currentPrice = client.getPrice(market.symbol());
      System.out.println("Current exchange: " + currentPrice.getPrice());
    } catch (Exception e) {
      println(
          "ERROR: Could not fetch current price: "
              + (e.getCause() != null ? e.getCause().toString() : e.toString()));
    }

    BigDecimal startPrice = config.getPrice();
    if (startPrice == null || startPrice.compareTo(BigDecimal.ZERO) <= 0) {
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

      BigDecimal currentTotalMoney = BigDecimal.ZERO;

      println("Account: ");
      for (AssetBalance a : balances) {
        if (market.gold().equalsIgnoreCase(a.getAsset())
            || market.money().equalsIgnoreCase(a.getAsset())) {
          BigDecimal total = a.getFree().add(a.getLocked());
          BigDecimal moneyTotal;
          if (market.gold().equalsIgnoreCase(a.getAsset())) {
            goldBalance = a;
            moneyTotal =
                currentPrice == null
                    ? BigDecimal.ZERO
                    : total
                        .multiply(new BigDecimal(currentPrice.getPrice()))
                        .setScale(8, RoundingMode.HALF_UP);
          } else {
            moneyBalance = a;
            moneyTotal = total;
          }

          currentTotalMoney = currentTotalMoney.add(moneyTotal);
          println(
              "  "
                  + a.getAsset()
                  + ": Available: "
                  + a.getFree()
                  + ", Reserved: "
                  + a.getLocked()
                  + ", Total: "
                  + total
                  + (market.gold().equalsIgnoreCase(a.getAsset())
                      ? " (" + moneyTotal + " " + market.money() + ")"
                      : ""));
        }
      }
      println(" Total: " + currentTotalMoney + " " + market.money());

      if (goldBalance != null) {
        if (config.getGold().compareTo(goldBalance.getFree()) > 0) {
          throw new IllegalArgumentException(
              "Trading limit for "
                  + goldBalance.getAsset()
                  + " ("
                  + config.getGold()
                  + ") exceeds available amount ("
                  + goldBalance.getFree()
                  + ")");
        }

        if (config.getGold().compareTo(BigDecimal.ZERO) == 0) {
          config.setGold(goldBalance.getFree());
        }
      }

      if (moneyBalance != null) {
        if (config.getMoney().compareTo(moneyBalance.getFree()) > 0) {
          throw new IllegalArgumentException(
              "Trading limit for "
                  + moneyBalance.getAsset()
                  + " ("
                  + config.getMoney()
                  + ") exceeds available amount ("
                  + moneyBalance.getFree()
                  + ")");
        }

        if (config.getMoney().compareTo(BigDecimal.ZERO) == 0) {
          config.setMoney(moneyBalance.getFree());
        }
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

  void printHelp() {
    println("Usage: nhmakerbot MARKET OPTIONS");
    println();
    println("Arguments:");
    println("  MARKET     Market symbol (e.g.: LTCBTC)");
    println();
    println("Options:");
    println(
        "  --gold-limit QUANTITY         The quantity limit of base asset (e.g. LTC in LTCBTC market)");
    println(
        "  --money-limit AMOUNT          The amount limit of quote asset (e.g. BTC in LTCBTC market)");
    println("  --price PRICE                 Target price around which to spread orders");
    println("  --low-price PRICE             Lower bound of targeted price range");
    println("  --high-price PRICE            Upper bound of targeted price range");
    println("  --price-pattern PATTERN       Price distribution to use when placing orders");
    println(
        "  --actions-per-second NUM      Maximum number of orders that can be placed per second");
    println("  --actions-limit NUM           Create the specified number of orders and exit");
    println(
        "  --ignore-limits               Don't take the decimal places limit into account (i.e. allow placing illegal orders)");
    println(
        "  --tick PRICE_PATTERN          Amount to serve as a multiplier with which to align all order prices");
    println(
        "  --type TYPE                   Limit orders to specific type. Possible values are: SELL, BUY");
    println(
        "  --events EVENTS               Comma-separated list of event types to follow on stdout.");
    println(
        "                                Possible values are: ORDER, TRADE, DEPTH, CANDLESTICK, TICKERS, or ALL");
  }

  void processEventsArg(Config config, String value) {
    String[] events = value.split(",");
    HashSet<EventType> enabledEvents = new HashSet<>();
    for (String event : events) {
      if ("ALL".equals(event)) {
        for (EventType t : EventType.values()) {
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

  void linkEventSources(Market market, Queue queue) {
    // Link up the queue with event sources
    ExchangeWebSocketClient wsClient = ExchangeClientFactory.newWebSocketClient(WS_ENDPOINT_URL);
    wsClient.onDepthEvent(
        market.symbol(),
        new ClientCallback<DepthEvent>() {
          @Override
          public void onResponse(DepthEvent result) {
            queue.add(new XDepthEvent(result));
          }

          @Override
          public void onFailure(Throwable t) {
            error("onDepthEvent FAILED: ", t);
          }
        });

    wsClient.onMarketTickerEvent(
        market.symbol(),
        new ClientCallback<AllMarketTickersEvent>() {
          @Override
          public void onResponse(AllMarketTickersEvent result) {
            queue.add(new XTickersEvent(result));
          }

          @Override
          public void onFailure(Throwable t) {
            error("onMarketTickerEvent FAILED: ", t);
          }
        });

    wsClient.onOrderBookEvent(
        market.symbol(),
        Levels.L5,
        new ClientCallback<OrderBook>() {
          @Override
          public void onResponse(OrderBook result) {
            queue.add(new XOrderEvent(result));
          }

          @Override
          public void onFailure(Throwable t) {
            error("onOrderBookEvent FAILED: ", t);
          }
        });

    wsClient.onCandlestickEvent(
        market.symbol(),
        CandlestickInterval.ONE_MINUTE,
        new ClientCallback<CandlestickEvent>() {
          @Override
          public void onResponse(CandlestickEvent result) {
            queue.add(new XCandlestickEvent(result));
          }

          @Override
          public void onFailure(Throwable t) {
            error("onCandlestickEvent FAILED: ", t);
          }
        });

    wsClient.onTradeEvent(
        market.symbol(),
        new ClientCallback<OrderTradeEvent>() {
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

  void printSummary(Config config, BigDecimal startPrice) {
    Market market = config.getMarket();
    println("Trading limits: " + config.getGold().toPlainString() + " " + market.gold());
    println("                  " + config.getMoney().toPlainString() + " " + market.money());
    println("Target price: " + startPrice);

    String lowPriceStr =
        new BigDecimal(config.getRelativeLowPricePct() * 100).setScale(0).toPlainString() + "%";
    lowPriceStr = lowPriceStr.startsWith("-") ? lowPriceStr : "+" + lowPriceStr;
    println("Low Price: " + (lowPrice != null ? lowPrice : lowPriceStr));

    String hiPriceStr =
        new BigDecimal(config.getRelativeHighPricePct() * 100).setScale(0).toPlainString() + "%";
    hiPriceStr = hiPriceStr.startsWith("-") ? hiPriceStr : "+" + hiPriceStr;
    println("High Price: " + (highPrice != null ? highPrice : hiPriceStr));

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

  ExchangeClient defaultExchangeClient() {
    OptionMap.Builder builder = defaultOptionBuilder();

    ExchangeClientFactory factory = ExchangeClientFactory.newInstance(builder.getMap());
    return factory.newClient();
  }

  protected OptionMap.Builder defaultOptionBuilder() {
    OptionMap.Builder builder =
        OptionMap.builder()
            .set(Options.BASE_URL, ENDPOINT_URL)
            .set(Options.WS_BASE_URL, WS_ENDPOINT_URL)
            .set(Options.KEY, API_KEY)
            .set(Options.SECRET, API_SECRET)
            .set(Options.READ_TIMEOUT, 60000);

    return builder;
  }

  protected void checkRequiredSettings(Config config) {
    if (API_KEY == null) {
      throw new RuntimeException("API KEY not set");
    }

    if (API_SECRET == null) {
      throw new RuntimeException("API SECRET not set");
    }

    if (config.getMarket() == null) {
      throw new RuntimeException("No market specified");
    }
  }

  double parseRelativePricePct(String arg) {
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
