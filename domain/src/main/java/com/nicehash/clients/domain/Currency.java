package com.nicehash.clients.domain;

import java.math.BigDecimal;
import java.math.BigInteger;


public enum Currency {

    BTC("TBTC", 8, "Bitcoin", false, 1, true),
    ETH("TETH", 18, "Ethereum", false, 3, true),
    XRP("TXRP", 6, "Ripple", false, 4, true),
    BCH("TBCH", 8, "Bitcoin cash", false, 5, true),
    LTC("TLTC", 8, "Litecoin", false, 6, true),
    ZEC("TZEC", 8, "Zcash", false, 7, true),
    DASH("TDASH", 8, "Dash", false, 8, true),
    XLM("TXLM", 7, "Stellar", false, 9, true),
    EOS("TEOS", 4, "EOS", false, 10, true),
    USDT("TERC", 6, "Tether", false, 11, true, ETH),
    BSV("TBSV", 8, "Bitcoin SV", false, 12, true),
    LINK("TERC", 18, "ChainLink", false, 13, true, ETH),
    BAT("TERC", 18, "Basic Attention Token", false, 14, true, ETH),
    PAX("TERC", 18, "Paxos Standard", false, 15, true, ETH),
    ZRX("TERC", 18, "0x Token", false, 16, true, ETH),
    HOT("TERC", 18, "Holo Token", false, 17, true, ETH),
    OMG("TERC", 18, "OmiseGO", false, 18, true, ETH),
    REP("TERC", 18, "Augur", false, 19, true, ETH),
    NEXO("TERC", 18, "Nexo Token", false, 20, true, ETH),
    BTG("TBTG", 8, "Bitcoin Gold", false, 21, true),
	EURKM("TEURKM", 4, "Euro Kriptomat", false, 2, false),
    ENJ("TERC", 18, "Enjin", false, 22, true, ETH),
    MATIC("TERC", 18, "Matic", false, 23, true, ETH),
    ELF("TERC", 18, "ELF", false, 24, true, ETH),
    SNT("TERC", 18, "Status", false, 25, true, ETH),
    BNT("TERC", 18, "Bancor", false, 26, true, ETH),
    KNC("TERC", 18, "KyberNetwork", false, 27, true, ETH),
    MTL("TERC", 8, "Metal", false, 28, true, ETH),
    POLY("TERC", 18, "Polymath", false, 29, true, ETH),
    POWR("TERC", 6, "PowerLedger", false, 30, true, ETH),
    GTO("TERC", 5, "Gifto", false, 31, true, ETH),
	LOOM("TERC", 18, "Loom", false, 32, true, ETH),
	CVC("TERC", 8, "Civic", false, 33, true, ETH),
	AST("TERC", 4, "AirSwap", false, 34, true, ETH),
	PPT("TERC", 8, "Populous", false, 35, true, ETH),
	LRC("TERC", 18, "Loopring", false, 36, true, ETH),
	KEY("TERC", 18, "Selfkey", false, 37, true, ETH),
	STORJ("TERC", 8, "Storj", false, 38, true, ETH),
	STORM("TERC", 18, "Storm", false, 39, true, ETH),
	TNT("TERC", 8, "Tierion", false, 40, true, ETH),
	DATA("TERC", 18, "DATAcoin", false, 41, true, ETH),
	AOA("TERC", 18, "Aurora", false, 42, true, ETH),
	RDN("TERC", 18, "Raiden", false, 43, true, ETH),
	USDC("TERC", 6, "USD Coin", false, 44, true, ETH),

    // testnet currencies
    TBTC("BTC", 8, "Bitcoin TestNet", true, 1, true),
	TEURKM("EURKM", 4, "Euro Kriptomat TestNet", true, 2, true),
    TETH("ETH", 18, "Ethereum TestNet", true, 3, true),
    TXRP("XRP", 6, "Ripple TestNet", true, 4, true),
    TBCH("BCH", 8, "Bitcoin cash TestNet", true, 5, true),
    TLTC("LTC", 8, "Litecoin TestNet", true, 6, true),
    TZEC("ZEC", 8, "Zcash TestNet", true, 7, true),
    TDASH("DASH", 8, "Dash TestNet", true, 8, true),
    TXLM("XLM", 7, "Stellar TestNet", true, 9, true),
    TEOS("EOS", 4, "EOS TestNet", true, 10, true),
    TERC("USDT", 0, "ERC TestNet", true, 11, true, TETH),
    TBSV("BSV", 8, "Bitcoin SV TestNet", true, 12, true),
    TBTG("BTG", 8, "Bitcoin Gold TestNet", true, 13, true),

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
	private final boolean mobile;
    private final BigDecimal subunits;
    private final Currency base;
    private final int order;

    Currency(String alt, int scale, String description, boolean test, int order, boolean mobile) {
        this(alt, scale, description, test, order, mobile, null);
    }

    Currency(String alt, int scale, String description, boolean test, int order, boolean mobile, Currency base) {
        this.alt = alt;
        this.scale = scale;
        this.description = description;
        this.test = test;
        // The value of the BigDecimal is (unscaledVal × 10^-scale)
        this.subunits = new BigDecimal(BigInteger.ONE, -scale);
        this.order = order;
        this.mobile = mobile;
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
	 * @return is mobile of currency
	 */
	public boolean mobile() {
		return mobile;
	}

    /**
     * Get base currency (used by tokens) otherwise return self
     * @return
     */
    public Currency base() {
        return base != null ? base : this;
    }
}

