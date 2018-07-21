package com.nicehash.tools.cli.util;

public enum OrderType {

    MARKET_SELL("market-sell", "MARKET SELL"),
    MARKET_BUY("market-buy", "MARKET BUY"),
    LIMIT_SELL("limit-sell", "LIMIT SELL"),
    LIMIT_BUY("limit-buy", "LIMIT BUY");

    private String label;
    private String caption;

    OrderType(String label, String caption) {
        this.label = label;
        this.caption = caption;
    }

    public String label() {
        return label;
    }

    public String caption() {
        return caption;
    }

    public static OrderType fromLabel(String value) {
        switch (value) {
            case "market-sell":
                return MARKET_SELL;
            case "market-buy":
                return MARKET_BUY;
            case "limit-sell":
                return LIMIT_SELL;
            case "limit-buy":
                return LIMIT_BUY;
            default:
                throw new IllegalArgumentException("Unknown order type: " + value);
        }
    }

}
