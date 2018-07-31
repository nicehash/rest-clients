
Nicehash Exchange CLI Client for REST API
=========================================

A simple CLI client to create, list, and cancel orders.


Build the example
-----------------

To build a single executable jar with all dependencies run:

    mvn clean install


Install alias for `nhx` command
-------------------------------

    . set-alias.sh


Configure the API Key and Secret
--------------------------------

You can create your API Key at: https://test.nicehash.com/settings/keys

```
export HISTCONTROL=ignorespace
 export NICEHASH_API_KEY=YOUR_API_KEY
 export NICEHASH_API_SECRET=YOUR_API_SECRET
```


Get command help
----------------
    nhx
    nhx place-order