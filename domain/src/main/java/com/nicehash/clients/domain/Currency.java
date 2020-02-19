package com.nicehash.clients.domain;

import java.math.BigDecimal;
import java.math.BigInteger;


public enum Currency {

    BTC("TBTC", 8, "Bitcoin", false, 1),
    ETH("TETH", 18, "Ethereum", false, 2),
    XRP("TXRP", 6, "Ripple", false, 3),
    BCH("TBCH", 8, "Bitcoin cash", false, 4),
    LTC("TLTC", 8, "Litecoin", false, 5),
    ZEC("TZEC", 8, "Zcash", false, 6),
    DASH("TDASH", 8, "Dash", false, 7),
    XLM("TXLM", 7, "Stellar", false, 8),
    EOS("TEOS", 4, "EOS", false, 9),
    USDT("TERC", 6, "Tether", false, 10, ETH),
    BSV("TBSV", 8, "Bitcoin SV", false, 11),
    LINK("TERC", 18, "ChainLink", false, 12, ETH),
    BAT("TERC", 18, "Basic Attention Token", false, 13, ETH),
    PAX("TERC", 18, "Paxos Standard", false, 14, ETH),
    ZRX("TERC", 18, "0x Token", false, 15, ETH),
    HOT("TERC", 18, "Holo Token", false, 16, ETH),
    OMG("TERC", 18, "OmiseGO", false, 17, ETH),
    REP("TERC", 18, "Augur", false, 18, ETH),
    NEXO("TERC", 18, "Nexo Token", false, 19, ETH),
    BTG("TBTG", 8, "Bitcoin Gold", false, 20),
    EURKM("TEURKM", 4, "Euro Kriptomat", false, 21),
    ENJ("TERC", 18, "Enjin", false, 22, ETH),
    MATIC("TERC", 18, "Matic", false, 23, ETH),
    ELF("TERC", 18, "ELF", false, 24, ETH),
    SNT("TERC", 18, "Status", false, 25, ETH),
    BNT("TERC", 18, "Bancor", false, 26, ETH),
    KNC("TERC", 18, "KyberNetwork", false, 27, ETH),
    MTL("TERC", 8, "Metal", false, 28, ETH),
    POLY("TERC", 18, "Polymath", false, 29, ETH),

    // testnet currencies
    TBTC("BTC", 8, "Bitcoin test", true, 1),
    TETH("ETH", 18, "Ethereum test", true, 2),
    TXRP("XRP", 6, "Ripple test", true, 3),
    TBCH("BCH", 8, "Bitcoin cash test", true, 4),
    TLTC("LTC", 8, "Litecoin test", true, 5),
    TZEC("ZEC", 8, "Zcash test", true, 6),
    TDASH("DASH", 8, "Dash test", true, 7),
    TXLM("XLM", 7, "Stellar test", true, 8),
    TEOS("EOS", 4, "EOS test", true, 9),
    TERC("USDT", 0, "Test ERC", true, 10, TETH),
    TBSV("BSV", 8, "Test Bitcoin SV", true, 11),
    TBTG("BTG", 8, "Test Bitcoin Gold", true, 12),
    TEURKM("EURKM", 4, "Test Euro Kriptomat", true, 13),
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
    private final Currency base;
    private final int order;

    Currency(String alt, int scale, String description, boolean test, int order) {
        this(alt, scale, description, test, order, null);
    }

    Currency(String alt, int scale, String description, boolean test, int order, Currency base) {
        this.alt = alt;
        this.scale = scale;
        this.description = description;
        this.test = test;
        // The value of the BigDecimal is (unscaledVal × 10^-scale)
        this.subunits = new BigDecimal(BigInteger.ONE, -scale);
        this.order = order;
        this.base = base;
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

    /**
     * Get base currency (used by tokens) otherwise return self
     * @return
     */
    public Currency base() {
        return base != null ? base : this;
    }
}

