# NiceHash REST Clients

Build the whole project:

    mvn clean install -DskipTests

Using Exchange client:

        OptionMap options = OptionMap.builder()
                                     .set(Options.BASE_URL, "https://api-test.nicehash.com/exchange/")
                                     .set(Options.KEY, "<KEY>")
                                     .set(Options.SECRET, "<SECRET>")
                                     .getMap();

        ExchangeClientFactory factory = ExchangeClientFactory.newInstance(options);
        ExchangeClient client = factory.newClient();
        List<Trade> trades = client.getMyTrades("LTCBTC");

Using Exchange async client:

        OptionMap options = OptionMap.builder()
                                     .set(Options.BASE_URL, "https://api-test.nicehash.com/exchange/")
                                     .set(Options.KEY, "<KEY>")
                                     .set(Options.SECRET, "<SECRET>")
                                     .getMap();

        client.getMyTrades("LTCBTC", 100, new AbstractClientCallback<List<Trade>>() {
            @Override
            public void onResponse(List<Trade> trades) {
                System.out.println("Trades = " + trades);
            }
        });

Using Exchange web-sockets:

        try (ExchangeWebSocketClient client = ExchangeClientFactory.newWebSocketClient("https://exchange-test.nicehash.com/ws")) {
            client.onDepthEvent("LTCBTC", new AbstractClientCallback<DepthEvent>() {
                @Override
                public void onResponse(DepthEvent result) {
                    System.out.println("Result = " + result);
                }
            });
        }
