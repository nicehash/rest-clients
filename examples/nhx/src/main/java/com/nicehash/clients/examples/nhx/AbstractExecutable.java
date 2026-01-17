package com.nicehash.clients.examples.nhx;

import com.nicehash.clients.common.spi.Options;
import com.nicehash.clients.exchange.ExchangeClient;
import com.nicehash.clients.exchange.ExchangeClientFactory;
import com.nicehash.clients.util.cli.CliUtils;
import com.nicehash.clients.util.options.OptionMap;

public abstract class AbstractExecutable {

  public static final String NICEHASH_URL =
      CliUtils.getNicehashUrl("https://api-test.nicehash.com/exchange");

  public static final String API_KEY = CliUtils.getApiKey(null);
  public static final String API_SECRET = CliUtils.getApiSecret(null);

  String command = System.getProperty("nicehash.cli.command");
  String subcommand = System.getProperty("nicehash.cli.subcommand");
  boolean passThroughErrors = subcommand != null;

  boolean verbose = false;

  static boolean isNetworkException(Throwable t) {
    return t instanceof java.net.SocketException;
  }

  protected abstract void execute(String[] args) throws Exception;

  public final void run(String[] args) throws Throwable {
    try {
      execute(args);
    } catch (Throwable e) {
      if (isNetworkException(e)) {
        e = new RuntimeException("Network error connecting to " + NICEHASH_URL, e);
      }
      if (passThroughErrors) {
        throw e;
      }
      CliUtils.printError(verbose, e);
      System.exit(1);
    }
  }

  protected void checkRequiredSettings() {
    if (API_KEY == null) {
      throw new RuntimeException("API KEY not set");
    }

    if (API_SECRET == null) {
      throw new RuntimeException("API SECRET not set");
    }
  }

  ExchangeClient defaultExchangeClient() {
    OptionMap.Builder builder = defaultOptionBuilder();

    ExchangeClientFactory factory = ExchangeClientFactory.newInstance(builder.getMap());
    return factory.newClient();
  }

  protected OptionMap.Builder defaultOptionBuilder() {
    OptionMap.Builder builder =
        OptionMap.builder()
            .set(Options.BASE_URL, NICEHASH_URL)
            .set(Options.KEY, API_KEY)
            .set(Options.SECRET, API_SECRET)
            .set(Options.READ_TIMEOUT, 60000);

    return builder;
  }
}
