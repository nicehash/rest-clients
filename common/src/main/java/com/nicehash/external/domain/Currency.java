package com.nicehash.external.domain;

/**
 * @author Ales Justin
 */
public enum Currency {

    BTC(20, 8, "Bitcoin", false, 1),
    ETH(20, 8, "Ethereum", false, 2),
    XRP(20, 8, "Ripple", false, 3), // TODO -- currently only for test, check actual subunits!
    BCH(20, 8, "Bitcoin cash", false, 4), // TODO -- currently only for test, check actual subunits!
    LTC(20, 8, "Litecoin", false, 5),
    ZEC(20, 8, "Zcash", false, 6), // TODO -- currently only for test, check actual subunits!

    // testnet currencies
    TBTC(20, 8, "Bitcoin test", true, 1),
    TETH(20, 8, "Ethereum test", true, 2),
    TXRP(20, 8, "Ripple test", true, 3),
    TBCH(20, 8, "Bitcoin cash test", true, 4),
    TLTC(20, 8, "Litecoin test", true, 5),
    TZEC(20, 8, "Zcash test", true, 6);

    /**
     * Maximum {@link #precision()} of any currency in this enum.
     */
    public static final int MAX_PRECISION = 20;

    /**
     * Maximum {@link #scale()} of any currency in this enum.
     */
    public static final int MAX_SCALE = 8;

    private final int precision;
    private final int scale;
    private final String description;
    private final boolean test;
    private final int order;

    Currency(int precision, int scale, String description, boolean test, int order) {
        this.precision = precision;
        this.scale = scale;
        this.description = description;
        this.test = test;
        this.order = order;
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
     * TODO: do we really need this? What's wrong with {@link #ordinal()}?
     *
     * @return an order (of what?)
     */
    @Deprecated
    public int order() {
        return order;
    }
}
