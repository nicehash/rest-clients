package com.nicehash.common.domain;

import java.math.BigDecimal;

/**
 * @author Ales Justin
 */
public enum Currency {

    BTC("TBTC", 20, 8, "Bitcoin", false, 1),
    ETH("TETH", 20, 8, "Ethereum", false, 2),
    XRP("TXRP", 20, 8, "Ripple", false, 3), // TODO -- currently only for test, check actual subunits!
    BCH("TBCH", 20, 8, "Bitcoin cash", false, 4), // TODO -- currently only for test, check actual subunits!
    LTC("TLTC", 20, 8, "Litecoin", false, 5),
    ZEC("TZEC", 20, 8, "Zcash", false, 6), // TODO -- currently only for test, check actual subunits!

    // testnet currencies
    TBTC("BTC", 20, 8, "Bitcoin test", true, 1),
    TETH("ETH", 20, 8, "Ethereum test", true, 2),
    TXRP("XRP", 20, 8, "Ripple test", true, 3),
    TBCH("BCH", 20, 8, "Bitcoin cash test", true, 4),
    TLTC("LTC", 20, 8, "Litecoin test", true, 5),
    TZEC("ZEC", 20, 8, "Zcash test", true, 6);

    /**
     * Maximum {@link #precision()} of any currency in this enum.
     */
    public static final int MAX_PRECISION = 20;

    /**
     * Maximum {@link #scale()} of any currency in this enum.
     */
    public static final int MAX_SCALE = 8;

    /**
     * Default subunits.
     */
    public static final int SUBUNITS = 10_000_000;

    private final String alt;
    private final int precision;
    private final int scale;
    private final String description;
    private final boolean test;
    private final BigDecimal subunits;
    private final int order;

    Currency(String alt, int precision, int scale, String description, boolean test, int order) {
        this(alt, precision, scale, description, test, SUBUNITS, order);
    }

    Currency(String alt, int precision, int scale, String description, boolean test, int subunits, int order) {
        this.alt = alt;
        this.precision = precision;
        this.scale = scale;
        this.description = description;
        this.test = test;
        this.subunits = new BigDecimal(subunits);
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
     * @return precision used to maintain amounts of this currency in
     */
    public int precision() {
        return precision;
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
     * TODO: do we really need this? What's wrong with {@link #ordinal()}?
     *
     * @return an order (of what?)
     */
    @Deprecated
    public int order() {
        return order;
    }
}
