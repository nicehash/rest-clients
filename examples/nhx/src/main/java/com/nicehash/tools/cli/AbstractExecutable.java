package com.nicehash.tools.cli;

import com.nicehash.utils.cli.CliUtils;
import com.nicehash.exchange.client.ExchangeClient;
import com.nicehash.exchange.client.ExchangeClientFactory;
import com.nicehash.external.spi.Options;
import com.nicehash.utils.options.OptionMap;


public abstract class AbstractExecutable {

    String endpointUrl = CliUtils.getNicehashUrl("https://api-test.nicehash.com/exchange");

    String key = CliUtils.getApiKey(null);
    String secret = CliUtils.getApiSecret(null);

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
                e = new RuntimeException("Network error connecting to " + endpointUrl, e);
            }
            if (passThroughErrors) {
                throw e;
            }
            CliUtils.printError(verbose, e);
            System.exit(1);
        }
    }

    void checkRequiredSettings() {
        if (key == null) {
            throw new RuntimeException("API KEY not set");
        }

        if (secret == null) {
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
            .set(Options.BASE_URL, endpointUrl)
            .set(Options.KEY, key)
            .set(Options.SECRET, secret)
            .set(Options.READ_TIMEOUT, 60000);

        return builder;
    }
}
