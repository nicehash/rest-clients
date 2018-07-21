package com.nicehash.tools.cli;

import com.nicehash.utils.cli.CliUtils;
import com.nicehash.exchange.client.ExchangeClient;
import com.nicehash.exchange.client.domain.account.request.CancelOrderRequest;


public class CancelOrder extends AbstractExecutable {

    static final String ARG_MARKET = "MARKET_SYMBOL";
    static final String ARG_ORDER_ID = "ORDER_ID";

    @Override
    void execute(String[] args) {

        String cmd = command != null ? command : "";
        cmd = cmd + " " + (subcommand != null ? subcommand : " cancel-order");

        if (args == null || args.length == 0) {
            System.out.println("Usage: " + cmd + " MARKET_SYMBOL ORDER_ID");
            System.out.println();
            System.out.println("Common market symbols are: ETHBTC, LTCBTC, XRPBTC, BCHBTC");
            System.out.println();
            System.out.println("Example:");
            System.out.println("   " + cmd + " LTCBTC 0aa759da-e5d5-4b15-b5ce-bee841c2c00f");

            System.exit(1);
        }

        String market = CliUtils.ensureArgument(ARG_MARKET, args, 0).toUpperCase();
        String orderId = CliUtils.ensureArgument(ARG_ORDER_ID, args, 1).toUpperCase();

        checkRequiredSettings();
        ExchangeClient client = defaultExchangeClient();

        client.cancelOrder(new CancelOrderRequest(market, CliUtils.asUUID(ARG_ORDER_ID, orderId)));
    }

    public static void main(String[] args) {
        new CancelOrder().execute(args);
    }
}
