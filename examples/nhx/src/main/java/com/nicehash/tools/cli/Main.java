package com.nicehash.tools.cli;

import com.nicehash.utils.cli.CliUtils;

public class Main {

    public static void main(String[] args) {

        boolean verbose = true;

        if (args == null || args.length == 0) {
            System.out.println("Usage: nhx OPERATION OPTIONS");
            System.out.println();
            System.out.println("OPERATION is one of: place-order, cancel-order, get-orders, get-open-orders");
            System.exit(1);
        }

        System.setProperty("nicehash.cli.command", "nhx");

        try {
            String arg = args[0];

            switch (arg) {
                case "place-order": {
                    System.setProperty("nicehash.cli.subcommand", "place-order");
                    new PlaceOrder().run(CliUtils.shift(args));
                    break;
                }
                case "get-open-orders":
                case "get-orders": {
                    System.setProperty("nicehash.cli.subcommand", arg);
                    new GetOrders().run(CliUtils.shift(args));
                    break;
                }
                case "cancel-order": {
                    System.setProperty("nicehash.cli.subcommand", "cancel-order");
                    new CancelOrder().run(CliUtils.shift(args));
                    break;
                }
                default: {
                    System.out.println("Unknown operation: " + arg);
                }
            }
        } catch (Throwable e) {
            CliUtils.printError(verbose, e);
            System.exit(1);
        }
    }
}
