package com.nicehash.clients.examples.nhx;

import com.nicehash.clients.exchange.domain.account.Order;
import com.nicehash.clients.exchange.domain.account.request.OrderRequest;
import com.nicehash.clients.util.cli.CliUtils;
import com.nicehash.clients.exchange.ExchangeClient;
import com.nicehash.clients.exchange.domain.account.request.CancelOrderRequest;

import java.util.List;


public class CancelOrder extends AbstractExecutable {

    static final String ARG_MARKET = "MARKET_SYMBOL";
    static final String ARG_ORDER_ID = "ORDER_ID";

    @Override
    protected void execute(String[] args) {

        String cmd = command != null ? command : "";
        cmd = cmd + " " + (subcommand != null ? subcommand : " cancel-order");

        if (args == null || args.length == 0) {
            System.out.println("Usage: " + cmd + " MARKET_SYMBOL ORDER_ID");
            System.out.println("       " + cmd + " MARKET_SYMBOL --all");
            System.out.println();
            System.out.println("Common market symbols are: ETHBTC, LTCBTC, XRPBTC, BCHBTC");
            System.out.println("Using option --all results in cancelling all open orders for the specified market");
            System.out.println();
            System.out.println("Example:");
            System.out.println("   " + cmd + " LTCBTC 0aa759da-e5d5-4b15-b5ce-bee841c2c00f");

            System.exit(1);
        }


        String market = CliUtils.ensureArgument(ARG_MARKET, args, 0);
        String orderId = CliUtils.ensureArgument(ARG_ORDER_ID, args, 1);
        boolean all = false;

        if ("--all".equals(market)) {
            market = orderId;
            orderId = null;
            all = true;
        } else if ("--all".equals(orderId)) {
            orderId = null;
            all = true;
        }
        checkRequiredSettings();
        ExchangeClient client = defaultExchangeClient();

        market = market.toUpperCase();

        if (!all) {
            client.cancelOrder(new CancelOrderRequest(market, CliUtils.asUUID(ARG_ORDER_ID, orderId)));
        } else {
            // get all open orders
            // delete them one by one
            List<Order> orders = client.getOpenOrders(new OrderRequest(market));
            for (Order o: orders) {
                client.cancelOrder(new CancelOrderRequest(market, o.getOrderId()));
            }
        }
    }

    public static void main(String[] args) {
        new CancelOrder().execute(args);
    }
}
