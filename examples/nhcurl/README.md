
NHCurl - curl wrapper with authentication
=========================================

A `curl` wrapper that adds authentication headers, and a few helper facilities so that you can use `curl` with Exchange REST API.

Build the example
-----------------

To build a single executable jar with all dependencies run:

    mvn clean install


Install alias for `nhcurl` command
----------------------------------

    . set-alias.sh


Configure the API Key and Secret
--------------------------------

You can create your API Key at: https://test.nicehash.com/settings/keys

```
export HISTCONTROL=ignorespace
 export NICEHASH_API_KEY=YOUR_API_KEY
 export NICEHASH_API_SECRET=YOUR_API_SECRET
```


You can now use `nhcurl` the same way as `curl`.

Variable substitution is supported in `nhcurl` command line. Single quotes are required in this case, otherwise bash complains.

* default.recv.window ... default value for receive window
* timestamp ... current epoch time (time in millis since 1.1.1970 UTC)
* ws.key ... WebSockets random key


Usage examples
--------------

See how much of each crypto currency you have available on your account:

    nhcurl 'https://api-test.nicehash.com/exchange/api/v1/account?timestamp=${timestamp}'


Create a new limit buy order:

    nhcurl 'https://api-test.nicehash.com/exchange/api/v1/order?symbol=TLTCTBTC&side=BUY&type=LIMIT&quantity=10&price=0.0095&timestamp=${timestamp}' -X POST -H "Content-Length: 0"


Create a new limit sell order:

    nhcurl 'https://api-test.nicehash.com/exchange/api/v1/order?symbol=TLTCTBTC&side=SELL&type=LIMIT&quantity=10&price=0.011&timestamp=${timestamp}' -X POST -H "Content-Length: 0"


See your order history:

    nhcurl 'https://api-test.nicehash.com/exchange/api/v1/allOrders?symbol=TLTCTBTC&limit=100&timestamp=${timestamp}'


See your open orders:

    nhcurl 'https://api-test.nicehash.com/exchange/api/v1/openOrders?symbol=TLTCTBTC&limit=100&timestamp=${timestamp}'


See your executed orders:

    nhcurl 'https://api-test.nicehash.com/exchange/api/v1/myTrades?symbol=TLTCTBTC&limit=100&timestamp=${timestamp}'


See all trades for specific market:

    nhcurl 'https://api-test.nicehash.com/exchange/api/v1/trades?symbol=TLTCTBTC&limit=500'


Open individual market trade stream:

    nhcurl 'https://exchange-test.nicehash.com/ws/TLTCTBTC@trade' -H "Connection: Upgrade" -H "Upgrade: websocket" -H "Host: exchange-test.nicehash.com" -H "Sec-WebSocket-Version: 13" -H 'Sec-WebSocket-Key: ${ws.key}' --no-buffer


Open aggregate trade stream:

    nhcurl 'https://exchange-test.nicehash.com/ws/TLTCTBTC@aggTrade' -H "Connection: Upgrade" -H "Upgrade: websocket" -H "Host: exchange-test.nicehash.com" -H "Sec-WebSocket-Version: 13" -H 'Sec-WebSocket-Key: ${ws.key}' --no-buffer


Open individual symbol ticker stream:

    nhcurl 'https://exchange-test.nicehash.com/ws/TLTCTBTC@ticker' -H "Connection: Upgrade" -H "Upgrade: websocket" -H "Host: exchange-test.nicehash.com" -H "Sec-WebSocket-Version: 13" -H 'Sec-WebSocket-Key: ${ws.key}' --no-buffer


Open all market tickers stream:

    nhcurl 'https://exchange-test.nicehash.com/ws/!ticker@arr' -H "Connection: Upgrade" -H "Upgrade: websocket" -H "Host: exchange-test.nicehash.com" -H "Sec-WebSocket-Version: 13" -H 'Sec-WebSocket-Key: ${ws.key}' --no-buffer


Open candlestick stream with chart interval of 5 minutes:

    nhcurl 'https://exchange-test.nicehash.com/ws/TLTCTBTC@kline_5m' -H "Connection: Upgrade" -H "Upgrade: websocket" -H "Host: exchange-test.nicehash.com" -H "Sec-WebSocket-Version: 13" -H 'Sec-WebSocket-Key: ${ws.key}' --no-buffer


Open partial book depth stream - level 5:

    nhcurl 'https://exchange-test.nicehash.com/ws/TLTCTBTC@depth5' -H "Connection: Upgrade" -H "Upgrade: websocket" -H "Host: exchange-test.nicehash.com" -H "Sec-WebSocket-Version: 13" -H 'Sec-WebSocket-Key: ${ws.key}' --no-buffer


Open differential depth stream - updated every second:

    nhcurl 'https://exchange-test.nicehash.com/ws/TLTCTBTC@depth' -H "Connection: Upgrade" -H "Upgrade: websocket" -H "Host: exchange-test.nicehash.com" -H "Sec-WebSocket-Version: 13" -H 'Sec-WebSocket-Key: ${ws.key}' --no-buffer





For Exchange REST API reference see documentation at: https://docs-test.nicehash.com

