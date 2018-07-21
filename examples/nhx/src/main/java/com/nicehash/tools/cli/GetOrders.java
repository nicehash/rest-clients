package com.nicehash.tools.cli;

import com.nicehash.utils.cli.CliUtils;
import com.nicehash.exchange.client.ExchangeClient;
import com.nicehash.exchange.client.domain.account.Order;
import com.nicehash.exchange.client.domain.account.request.AllOrdersRequest;
import com.nicehash.exchange.client.domain.account.request.OrderRequest;

import java.util.List;


public class GetOrders extends AbstractExecutable {

    static final String CMD_GET_ORDERS = "get-orders";
    static final String CMD_GET_OPEN_ORDERS = "get-open-orders";
    static final String ARG_MARKET = "MARKET_SYMBOL";


    @Override
    void execute(String[] args) {

        if (subcommand == null) {
            throw new RuntimeException("System property 'nicehash.cli.subcommand' not set");
        }

        String cmd = command != null ? command : "";
        cmd = cmd + " " + subcommand;

        if (args == null || args.length == 0) {
            printHelp(cmd);
            System.exit(1);
        }

        String market = CliUtils.ensureArgument(ARG_MARKET, args, 0).toUpperCase();

        checkRequiredSettings();
        ExchangeClient client = defaultExchangeClient();

        switch (subcommand) {
            case CMD_GET_ORDERS: {
                // TODO: market symbol should be optional. User may be interested in complete
                // trading history, not just for single market
                List<Order> orders = client.getAllOrders(new AllOrdersRequest(market));
                for (Order o : orders) {
                    System.out.println(" - " + o);
                }
                break;
            }
            case CMD_GET_OPEN_ORDERS: {
                List<Order> orders = client.getOpenOrders(new OrderRequest(market));
                for (Order o : orders) {
                    System.out.println(" - " + o);
                }
                break;
            }
            default: {
                throw new RuntimeException("Unknown operation: " + subcommand);
            }
        }
    }

    private static void printHelp(String cmd) {
        System.out.println("Usage: " + cmd + " MARKET_SYMBOL");
        System.out.println();
        System.out.println("Common market symbols are: ETHBTC, LTCBTC, XRPBTC, BCHBTC");
        System.out.println();
        System.out.println("Example:");
        System.out.println("   " + cmd + " LTCBTC");
    }

    public static void main(String[] args) {
        System.setProperty("nicehash.cli.subcommand", "get-orders");
        new GetOrders().execute(args);
    }
}
