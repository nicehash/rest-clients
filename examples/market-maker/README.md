A simple market making bot
==========================

This example shows hot to how to make orders, and how to register for and consume websockets events.


Build the example
-----------------

To build a single executable jar with all dependencies run:

    mvn clean install

Install alias for `nhmakerbot` command
--------------------------------------

    . set-alias.sh


Configure API Key and Secret
----------------------------

Then set api key and secret as env variables:

```
export HISTCONTROL=ignorespace
 export NICEHASH_API_KEY=YOUR_API_KEY
 export NICEHASH_API_SECRET=YOUR_API_SECRET
```


Run it
------

    nhmakerbot TLTCTBTC