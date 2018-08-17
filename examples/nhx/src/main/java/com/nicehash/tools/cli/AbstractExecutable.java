package com.nicehash.tools.cli;

import com.nicehash.utils.cli.CliUtils;
import com.nicehash.exchange.client.ExchangeClient;
import com.nicehash.exchange.client.ExchangeClientFactory;
import com.nicehash.external.spi.Options;
import com.nicehash.utils.options.OptionMap;


public abstract class AbstractExecutable {

    static final String NICEHASH_URL = CliUtils.getNicehashUrl("https://api-test.nicehash.com/exchange");

    static final String API_KEY = CliUtils.getApiKey(null);
    static final String API_SECRET = CliUtils.getApiSecret(null);

    String command = System.getProperty("nicehash.cli.command");
    String subcommand = System.getProperty("nicehash.cli.subcommand");
    boolean passThroughErrors = subcommand != null;

    boolean verbose = false;


    static boolean isNetworkException(Throwable t) {
        return t instanceof java.net.SocketException;
    }

    abstract void execute(String[] args) throws Exception;

    void run(String[] args) throws Throwable {
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

    void checkRequiredSettings() {
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

    OptionMap.Builder defaultOptionBuilder() {
        OptionMap.Builder builder = OptionMap.builder()
            .set(Options.BASE_URL, NICEHASH_URL)
            .set(Options.KEY, API_KEY)
            .set(Options.SECRET, API_SECRET)
            .set(Options.READ_TIMEOUT, 60000);

        return builder;
    }
}
