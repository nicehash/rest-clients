package com.nicehash.utils.cli;

public class Market {

    private String symbol;
    private String gold;
    private String money;

    public Market(String symbol) {
        if (symbol == null) {
            throw new IllegalArgumentException("symbol == null");
        }
        this.symbol = symbol;
        int mid = symbol.length() / 2;
        if (symbol.length() % 2 == 1) {
            if (Character.toLowerCase(symbol.charAt(0)) == 't') {
                mid += 1;
            }
        }
        gold = symbol.substring(0, mid);
        money = symbol.substring(mid);
    }

    public String gold() {
        return gold;
    }

    public String money() {
        return money;
    }

    public String symbol() {
        return symbol;
    }
}
