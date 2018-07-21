
NHCurl - curl wrapper with authentication
=========================================

A `curl` wrapper that adds authentication headers, and a few helper facilities so that you can use `curl` with Exchange REST API.

To build use:

    mvn clean install

To install `nhcurl` first run from exchange root:

    . set-alias.sh


Then set api key and secret as env variables:

```
export HISTCONTROL=ignorespace
 export NICEHASH_API_KEY=YOUR_API_KEY
 export NICEHASH_API_SECRET=YOUR_API_SECRET
```


You can now use `nhcurl` the same way as `curl`.

Variable substitution is supported in `nhcurl` command line. Single quotes are required in this case, otherwise bash complains.

* default.recv.window ... default value for receive window
* timestamp ... current epoch (time in millis since 1.1.1970 UTC)
* ws.key ... WebSockets random key


Usage examples
--------------

See how much of each crypto currency you have available on your account:

    nhcurl 'https://api-test.nicehash.com/exchange/api/v1/account?timestamp=${timestamp}'


See your order history:

    nhcurl 'https://api-test.nicehash.com/exchange/api/v1/allOrders?symbol=TLTCTBTC&limit=100&timestamp=${timestamp}'


See your open orders:

    nhcurl 'https://api-test.nicehash.com/exchange/api/v1/openOrders?symbol=TLTCTBTC&limit=100&timestamp=${timestamp}'


See your executed orders:

    nhcurl 'https://api-test.nicehash.com/exchange/api/v1/myTrades?symbol=TLTCTBTC&limit=100&timestamp=${timestamp}'


See all trades for specific market:

    nhcurl 'https://api-test.nicehash.com/exchange/api/v1/trades?symbol=TLTCTBTC&limit=500'


Initiate user data stream - receive a listen key:

    nhcurl 'https://api-test.nicehash.com/exchange/api/v1/userDataStream' -X POST -H "Content-Length: 0"


Ping user data stream to keep it alive - using the listen key:

    nhcurl 'https://api-test.nicehash.com/exchange/api/v1/userDataStream?listenKey=89aff272-ebd3-436d-a341-bac3e15068b7' -X PUT -H 'Content-Length: 0' -v


Delete user data stream

    nhcurl 'https://api-test.nicehash.com/exchange/api/v1/userDataStream?listenKey=89aff272-ebd3-436d-a341-bac3e15068b7' -X DELETE -v


Open user data websocket stream (for some reason connection gets closed immediately):

    nhcurl https://exchange-test.nicehash.com/ws/89aff272-ebd3-436d-a341-bac3e15068b7 -H "Connection: Upgrade" -H "Upgrade: websocket" -H "Host: exchange-test.nicehash.com" -H "Sec-WebSket-Version: 13" -H 'Sec-WebSocket-Key: ${ws.key}' --no-buffer


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





For Exchange REST API reference see documentation at:

    TODO: Documentation URL

