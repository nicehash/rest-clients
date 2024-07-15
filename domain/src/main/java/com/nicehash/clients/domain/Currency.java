package com.nicehash.clients.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;


public enum Currency {
  
    BTC("TBTC", 8, "Bitcoin", false, 1),
    ETH("TETH", 18, "Ethereum", false, 3),
    XRP("TXRP", 6, "Ripple", false, 4),
    BCH("TBCH", 8, "Bitcoin cash", false, 5),
    LTC("TLTC", 8, "Litecoin", false, 6),
    ZEC("TZEC", 8, "Zcash", false, 7),
    DASH("TDASH", 8, "Dash", false, 8),
    XLM("TXLM", 7, "Stellar", false, 9),
    EOS("TEOS", 4, "EOS", false, 10),
    USDT("TERC", 6, "Tether", false, 11, Set.of(ETH)),
    BSV("TBSV", 8, "Bitcoin SV", false, 12),
    LINK("TERC", 18, "ChainLink", false, 13, Set.of(ETH)),
    @Deprecated BAT("TERC", 18, "Basic Attention Token", false, 14, Set.of(ETH)),
    PAX("TERC", 18, "Paxos Standard", false, 15, Set.of(ETH)),
    ZRX("TERC", 18, "0x Token", false, 16, Set.of(ETH)),
    HOT("TERC", 18, "Holo Token", false, 17, Set.of(ETH)),
    OMG("TERC", 18, "OmiseGO", false, 18, Set.of(ETH)),
    REP("TERC", 18, "Augur", false, 19, Set.of(ETH)),
    NEXO("TERC", 18, "Nexo Token", false, 20, Set.of(ETH)),
    BTG("TBTG", 8, "Bitcoin Gold", false, 21),
    @Deprecated EURKM("TEURKM", 4, "Euro Kriptomat", false, 82),
    @Deprecated ENJ("TERC", 18, "Enjin", false, 22, Set.of(ETH)),
    MATIC("TERC", 18, "Matic", false, 23, Set.of(ETH)),
    ELF("TERC", 18, "ELF", false, 24, Set.of(ETH)),
    SNT("TERC", 18, "Status", false, 25, Set.of(ETH)),
    BNT("TERC", 18, "Bancor", false, 26, Set.of(ETH)),
    @Deprecated KNC("TERC", 18, "KyberNetwork", false, 27, Set.of(ETH)),
    MTL("TERC", 8, "Metal", false, 28, Set.of(ETH)),
    POLY("TERC", 18, "Polymath", false, 29, Set.of(ETH)),
    POWR("TERC", 6, "PowerLedger", false, 30, Set.of(ETH)),
    GTO("TERC", 5, "Gifto", false, 31, Set.of(ETH)),
    LOOM("TERC", 18, "Loom", false, 32, Set.of(ETH)),
    CVC("TERC", 8, "Civic", false, 33, Set.of(ETH)),
    @Deprecated AST("TERC", 4, "AirSwap", false, 34, Set.of(ETH)),
    PPT("TERC", 8, "Populous", false, 35, Set.of(ETH)),
    LRC("TERC", 18, "Loopring", false, 36, Set.of(ETH)),
    KEY("TERC", 18, "Selfkey", false, 37, Set.of(ETH)),
    STORJ("TERC", 8, "Storj", false, 38, Set.of(ETH)),
    STORM("TERC", 18, "Storm", false, 39, Set.of(ETH)),
    TNT("TERC", 8, "Tierion", false, 40, Set.of(ETH)),
    DATA("TERC", 18, "DATAcoin", false, 41, Set.of(ETH)),
    AOA("TERC", 18, "Aurora", false, 42, Set.of(ETH)),
    RDN("TERC", 18, "Raiden", false, 43, Set.of(ETH)),
    USDC("TERC", 6, "USD Coin", false, 44, Set.of(ETH)),
  	FET("TERC", 18, "Fetch", false, 45, Set.of(ETH)),
    ANT("TERC", 18, "Aragon", false, 46, Set.of(ETH)),
    AERGO("TERC", 18, "Aergo", false, 47, Set.of(ETH)),
    LBA("TERC", 18, "Libra", false, 48, Set.of(ETH)),
    XMR("TXMR", 12, "Monero", false, 49),
    MITH("TERC", 18, "Mithril", false, 50, Set.of(ETH)),
    @Deprecated BAND("TERC", 18, "Band", false, 51, Set.of(ETH)),
    SXP("TERC", 18, "Swipe", false, 52, Set.of(ETH)),
    EURS("TERC", 2, "Stasis EURO", false, 53, Set.of(ETH)),
    WBTC("TERC", 8, "Wrapped BTC", false, 54, Set.of(ETH)),
    RVN("TRVN", 8, "Raven Coin", false, 55),
    UNI("TERC", 18, "Uniswap", false, 56, Set.of(ETH)),
    AAVE("TERC", 18, "Aeve", false, 57, Set.of(ETH)),
    FTM("TERC", 18, "Fantom", false, 58, Set.of(ETH)),
    YFI("TERC", 18, "yearn.finance", false, 59, Set.of(ETH)),
    DOGE("TDOGE", 8, "Dogecoin", false, 60),
    ONEINCH("TERC", 18, "1INCH", false, 61, Set.of(ETH)),
    SUSHI("TERC", 18, "Sushi", false, 62, Set.of(ETH)),
    OCEAN("TERC", 18, "Ocean", false, 63, Set.of(ETH)),
    @Deprecated MKR("TERC", 18, "Maker", false, 64, Set.of(ETH)),
    CRV("TERC", 18, "Curve DAO", false, 65, Set.of(ETH)),
    CHZ("TERC", 18, "chiliZ", false, 66, Set.of(ETH)),
    GRT("TERC", 18, "The Graph", false, 67, Set.of(ETH)),
    GNO("TERC", 18, "Gnosis", false, 68, Set.of(ETH)),
    HBAR("THBAR", 8, "Hedera Hashgraph", false, 69),
    ADA("TADA", 6, "Cardano", false, 70),
    ALGO("TALGO", 6, "Algorand", false, 71),
    XTZ("TXTZ", 6, "Tezos", false, 72),
    SAND("TERC", 18, "Sand", false, 73, Set.of(ETH)),
    SHIB("TERC", 18, "Shiba Inu", false, 74, Set.of(ETH)),
    STX("TSTX", 6, "Stacks", false, 75),
    GALA("TERC", 8, "Gala", false, 76, Set.of(ETH)),
    SOL("TSOL", 9, "Solana", false, 77),
    DOT("TDOT", 10, "Polkadot", false, 78),
    ETC("TETC", 18, "Ethereum Classic", false, 79),
    TRX("TTRX", 6, "Tron", false, 80),
    KSM("KSM", 12, "Kusama", false, 81),
    EUR("TEUR", 2, "Euro", false, 2),

    // testnet currencies
    TBTC("BTC", 8, "Bitcoin TestNet", true, 1),
    TETH("ETH", 18, "Ethereum TestNet", true, 3),
    TXRP("XRP", 6, "Ripple TestNet", true, 4),
    TBCH("BCH", 8, "Bitcoin cash TestNet", true, 5),
    TLTC("LTC", 8, "Litecoin TestNet", true, 6),
    TZEC("ZEC", 8, "Zcash TestNet", true, 7),
    TDASH("DASH", 8, "Dash TestNet", true, 8),
    TXLM("XLM", 7, "Stellar TestNet", true, 9),
    TEOS("EOS", 4, "EOS TestNet", true, 10),
    TERC("USDT", 0, "ERC TestNet", true, 11, Set.of(TETH)),
    TBSV("BSV", 8, "Bitcoin SV TestNet", true, 12),
    TBTG("BTG", 8, "Bitcoin Gold TestNet", true, 13),
    @Deprecated TEURKM("EURKM", 4, "Euro Kriptomat TestNet", true, 27),
    TXMR("XMR", 12, "Monero Testnet", true, 14),
    TRVN("RVN", 8, "Raven Testnet", true, 15),
    TDOGE("DOGE", 8, "Dogecoin Testnet", true, 16),
    THBAR("HBAR", 8, "Hedera Testnet", true, 17),
    TADA("ADA", 6, "Cardano Testnet", true, 18),
    TALGO("ALGO", 6, "Algorand testnet", true, 19),
    TXTZ("XTZ", 6, "Tezos testnet", true, 20),
    TSTX("STX", 6, "Stacks testnet", true, 21),
    TTTT("USDT", 6, "ERC Testnet", true, 22, Set.of(TETH)),
    TSOL("SOL", 9, "Solana Testnet", true, 23),
    TDOT("DOT", 10, "Polkadot Testnet", true, 24),
    TETC("ETC", 18, "Ethereum Classic", true, 25),

    TTRX("TRX", 6, "Tron Testnet", true, 26),
    TEUR("EUR", 2, "Euro TestNet", true, 2),
    TMARC("USDT", 18, "MarcPride", true, 28, Set.of(TETH)),
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
    private final Set<Currency> base;
    private final int order;

    Currency(String alt, int scale, String description, boolean test, int order) {
        this(alt, scale, description, test, order, null);
    }

    Currency(String alt, int scale, String description, boolean test, int order, Set<Currency> base) {
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
        return (alt != null) ? valueOfOptimized(alt) : null;
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
    public Set<Currency> base() {
        return base != null ? base : Set.of(this);
    }

    /**
     * Fast retrival of currency from name
     */
    private static final HashMap<String, com.nicehash.clients.domain.Currency> staticMap;
    static {
        staticMap = new HashMap<>();
        EnumSet<Currency> set = EnumSet.allOf(Currency.class);
        for(Currency c: set) {
            staticMap.put(c.name(), c);
        }
    }

    public static Currency valueOfOptimized(String name) {
        Currency c = staticMap.get(name);
        if (c == null) {
            throw new IllegalArgumentException("No enum constant " + Currency.class.getCanonicalName() + "." + name);
        }
        return c;
    }
}
