A simple market making bot
==========================

This example shows how to make orders, and how to register for and consume websockets events.


Build the example
-----------------

To build a single executable jar with all dependencies run:

    mvn clean install


Install alias for `nhmakerbot` command
--------------------------------------

    . set-alias.sh


Configure the API Key and Secret
--------------------------------

You can create your API Key at: https://test.nicehash.com/settings/keys

```
export HISTCONTROL=ignorespace
 export NICEHASH_API_KEY=YOUR_API_KEY
 export NICEHASH_API_SECRET=YOUR_API_SECRET
```


Run it
------

Print usage help

    nhmakerbot

Place buy and sell orders in random quantities and prices within +/- 20% around the specified price, and display all ORDER and TRADE events

    nhmakerbot TLTCTBTC --price 0.01 --events ORDER,TRADE

Place buy and sell orders in random quantities and prices within the specified boundaries around current market price at a rate once every ten seconds:

    nhmakerbot TLTCTBTC --low-price -0.05% --high-price +0.05% --actions-per-second 0.1

Place ten sell orders in random quantities at exactly the specified price, then exit:

    nhmakerbot TLTCTBTC --price 0.0099 --exact --type SELL --actions-limit 10

Place buy and sell orders in random quantities and prices within the specified boundaries in a V shape probabilistic distribution with specified price tick

    nhmakerbot TLTCTBTC --low-price 0.0085 --high-price 0.0115 --price-pattern 432101234 --tick 0.00005

Same as previous but place buy orders below target price, and sell orders above target price, if you run out of funds cancel some existing order

    nhmakerbot TLTCTBTC --low-price 0.0085 --high-price 0.0115 --price-pattern 432101234 --tick 0.00005 --price 0.01 --no-take

Same as precious but place orders in the shape of an inverted V, and if you run out of funds cancel some existing order with same side of trade

    nhmakerbot TLTCTBTC --low-price 0.0085 --high-price 0.0115 --price-pattern 123454321 --tick 0.00005 --price 0.01 --no-take --cancel-on-limit