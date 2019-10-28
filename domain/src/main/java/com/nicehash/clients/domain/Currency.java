package com.nicehash.clients.domain;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Ales Justin
 */
public enum Currency {

    BTC("TBTC", 8, "Bitcoin", false, 1),
    ETH("TETH", 18, "Ethereum", false, 2),
    XRP("TXRP", 6, "Ripple", false, 3),
    BCH("TBCH", 8, "Bitcoin cash", false, 4),
    LTC("TLTC", 8, "Litecoin", false, 5),
    ZEC("TZEC", 8, "Zcash", false, 6),
    DASH("TDASH", 8, "Dash", false, 7),
    XLM("XLM", 7, "Stellar", false, 8),

    // testnet currencies
    TBTC("BTC", 8, "Bitcoin test", true, 1),
    TETH("ETH", 18, "Ethereum test", true, 2),
    TXRP("XRP", 6, "Ripple test", true, 3),
    TBCH("BCH", 8, "Bitcoin cash test", true, 4),
    TLTC("LTC", 8, "Litecoin test", true, 5),
    TZEC("ZEC", 8, "Zcash test", true, 6),
    TDASH("DASH", 8, "Dash test", true, 7),
    TXLM("XLM", 7, "Stellar test", true, 8),
    TERC(null, 6, "Test ERC", true, 9)
    ;

    /**
     * Maximum precision in DB
     */
    public static final int MAX_PRECISION = 30;

    /**
     * Maximum {@link #scale()} of any currency in this enum.
     */
    public static final int MAX_SCALE = 18;

    /**
     * Price scale
     */
    public static final int PRICE_SCALE = 8;

    private final String alt;
    private final int scale;
    private final String description;
    private final boolean test;
    private final BigDecimal subunits;
    private final int order;

    Currency(String alt, int scale, String description, boolean test, int order) {
        this.alt = alt;
        this.scale = scale;
        this.description = description;
        this.test = test;
        // The value of the BigDecimal is (unscaledVal × 10^-scale)
        this.subunits = new BigDecimal(BigInteger.ONE, -scale);
        this.order = order;
    }

    /**
     * Get alternative currency, if one exists.
     * e.g. its testing alternative; BTC <-> TBTC
     *
     * @return alternative currency or null if no such alternative
     */
    public Currency getAlt() {
        return (alt != null) ? valueOf(alt) : null;
    }

    /**
     * @return scale used to round amounts of this currency to
     */
    public int scale() {
        return scale;
    }

    /**
     * @return human readable description of this currency
     * (as opposed to {@link #name()} which returns the name of the constant)
     */
    public String description() {
        return description;
    }

    /**
     * @return {@code true} if this is a test currency (used on testing setups to prevent confusion)
     */
    public boolean isTest() {
        return test;
    }

    /**
     * @return currency's subunits
     */
    public BigDecimal subunits() {
        return subunits;
    }

    /**
     * @return an order of currency
     */
    public int order() {
        return order;
    }
}

