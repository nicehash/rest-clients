package com.nicehash.tools.cli;

import com.nicehash.utils.cli.CliUtils;
import com.nicehash.utils.cli.Market;
import com.nicehash.tools.cli.util.OrderType;
import com.nicehash.exchange.client.ExchangeClient;
import com.nicehash.exchange.client.domain.TimeInForce;
import com.nicehash.exchange.client.domain.account.NewOrder;
import com.nicehash.exchange.client.domain.account.NewOrderResponse;

import static com.nicehash.utils.cli.CliUtils.ensureOption;
import static com.nicehash.utils.cli.CliUtils.missOption;

import java.math.BigDecimal;


public class PlaceOrder extends AbstractExecutable {

    static final String ARG_TYPE = "ORDER_TYPE";
    static final String ARG_MARKET = "MARKET_SYMBOL";
    static final String OPT_QUANTITY = "--quantity";
    static final String OPT_MAX_AMOUNT = "--max-amount";
    static final String OPT_PRICE = "--price";


    public void execute(String[] args) {

        OrderType type = null;
        String market = null;
        BigDecimal quantity = null;
        BigDecimal price = null;
        BigDecimal maxAmount = null;

        String cmd = command != null ? command : "";
        cmd = cmd + (subcommand != null ? " " + subcommand : " place-order");

        if (args == null || args.length == 0) {
            System.out.println("Usage: " + cmd + " ORDER_TYPE MARKET_SYMBOL [--quantity QUANTITY] [--price PRICE] [--max-amount MAX_AMOUNT]");
            System.out.println();
            System.out.println("ORDER_TYPE is one of: limit-buy, limit-sell, market-buy, market-sell");
            System.out.println("Common market symbols are: ETHBTC, LTCBTC, XRPBTC, BCHBTC");
            System.out.println();
            System.out.println("Example:");
            System.out.println("   " + cmd + " limit-buy LTCBTC --quantity 30 --price 0.257");
            System.out.println("   " + cmd + " limit-sell LTCBTC --quantity 20 --price 0.257");
            System.out.println("   " + cmd + " market-buy ETHBTC --quantity 4 --max-amount 2");
            System.out.println("   " + cmd + " market-buy ETHBTC --max-amount 2");
            System.out.println("   " + cmd + " market-sell ETHBTC --quantity 5");

            System.exit(1);
        }


        type = OrderType.fromLabel(CliUtils.ensureArgument(ARG_TYPE, args, 0));
        market = CliUtils.ensureArgument(ARG_MARKET, args, 1).toUpperCase();

        for (int i = 2; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case OPT_QUANTITY: {
                    quantity = new BigDecimal(CliUtils.optValue(arg, args, ++i));
                    break;
                }
                case OPT_PRICE: {
                    price = new BigDecimal(CliUtils.optValue(arg, args, ++i));
                    break;
                }
                case OPT_MAX_AMOUNT: {
                    maxAmount = new BigDecimal(CliUtils.optValue(arg, args, ++i));
                    break;
                }
                default:
                    throw new IllegalArgumentException("Invalid option: " + arg);
            }
        }

        checkRequiredSettings();
        ExchangeClient client = defaultExchangeClient();

        Market m = new Market(market);
        NewOrderResponse res = null;

        switch (type) {
            case LIMIT_BUY: {
                ensureOption(OPT_QUANTITY, quantity);
                ensureOption(OPT_PRICE, price);
                missOption(OPT_MAX_AMOUNT, maxAmount);
                res = client.newOrder(NewOrder.limitBuy(market, TimeInForce.GTC, quantity, price));
                System.out.println("Successfully placed a LIMIT BUY order for " + quantity + " " + m.gold() + " at price " + price + " " + m.money() + " for 1 " + m.gold() + "\n" + res.toString());
                break;
            }
            case LIMIT_SELL: {
                ensureOption(OPT_QUANTITY, quantity);
                ensureOption(OPT_PRICE, price);
                missOption(OPT_MAX_AMOUNT, maxAmount);
                res = client.newOrder(NewOrder.limitSell(market, TimeInForce.GTC, quantity, price));
                System.out.println("Successfully placed a LIMIT SELL order for " + quantity + " " + m.gold() + " at price " + price + " " + m.money() + " for 1 " + m.gold() + "\n" + res.toString());
                break;
            }
            case MARKET_BUY: {
                missOption(OPT_PRICE, price);
                if (quantity != null) {
                    if (maxAmount == null) {
                        maxAmount = new BigDecimal(Long.MAX_VALUE);
                    }
                    res = client.newOrder(NewOrder.marketBuy(market, quantity, maxAmount));
                    System.out.println("Successfully placed a MARKET BUY order for " + quantity + " " + m.gold() + ", not spending more than " + maxAmount + " " + m.money() + "\n" + res.toString());
                } else {
                    ensureOption(OPT_MAX_AMOUNT, maxAmount);
                    res = client.newOrder(NewOrder.marketBuy(market, maxAmount));
                    System.out.println("Successfully placed a MARKET BUY order for " + m.gold() + ", spending " + maxAmount + " " + m.money() + "\n" + res.toString());
                }
                break;
            }
            case MARKET_SELL: {
                ensureOption(OPT_QUANTITY, quantity);
                missOption(OPT_PRICE, price);
                missOption(OPT_MAX_AMOUNT, maxAmount);
                res = client.newOrder(NewOrder.marketSell(market, quantity));
                System.out.println("Successfully placed a MARKET SELL order of " + quantity + " " + m.gold() + "\n" + res.toString());
                break;
            }
        }
    }

    public static void main(String[] args) {
        new PlaceOrder().execute(args);
    }
}
