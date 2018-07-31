package com.nicehash.tools.marketmaker;

import com.nicehash.exchange.client.domain.OrderSide;
import com.nicehash.exchange.client.domain.account.AssetBalance;
import com.nicehash.tools.marketmaker.event.EventType;
import com.nicehash.utils.cli.Market;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Config {

    private Market market;
    private BigDecimal gold = BigDecimal.ZERO;
    private BigDecimal money = BigDecimal.ZERO;
    private String pricePattern;
    private int [] pricePatternArray = {1};
    private int pricePatternBlockCount = 1;
    private BigDecimal price = BigDecimal.ZERO;
    private double maxOrdersPerSec = 20;
    private boolean ignoreLimits;
    private double actionsPerSecond = 1;
    private double relativeLowPricePct = 0.2;
    private BigDecimal relativeLowPrice;
    private BigDecimal lowPrice;
    private double relativeHighPricePct = 0.2;
    private BigDecimal relativeHighPrice;
    private BigDecimal highPrice;
    private BigDecimal tick;
    private BigDecimal halfTick;
    private Set<EventType> enabledEvents;
    private OrderSide type;
    private int actionsLimit;
    private boolean noTake;

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public BigDecimal getGold() {
        return gold;
    }

    public void setGold(BigDecimal gold) {
        this.gold = gold;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getPricePattern() {
        return pricePattern;
    }

    public void setPricePattern(String pattern) {
        // must be all digits
        int[] blocks = new int[pattern.length()];
        int blockCount = 0;
        for (int i = 0; i < blocks.length; i++) {
            char c = pattern.charAt(i);
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("Invalid character in price pattern: " + c);
            }
            blocks[i] = c - (int)'0';
            blockCount += blocks[i];
        }

        this.pricePattern = pattern;
        this.pricePatternArray = blocks;
        this.pricePatternBlockCount = blockCount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public double getMaxOrdersPerSec() {
        return maxOrdersPerSec;
    }

    public void setMaxOrdersPerSec(double maxOrdersPerSec) {
        this.maxOrdersPerSec = maxOrdersPerSec;
    }

    public void setIgnoreLimits(boolean ignoreLimits) {
        this.ignoreLimits = ignoreLimits;
    }

    public boolean isIgnoreLimits() {
        return this.ignoreLimits;
    }

    public double getActionsPerSecond() {
        return actionsPerSecond;
    }

    public void setActionsPerSecond(double actionsPerSecond) {
        this.actionsPerSecond = actionsPerSecond;
    }

    public void applyPrecisionLimitsIfNecessary(int decimals, RoundingMode mode) {
        if (!ignoreLimits) {
            if (gold != null)
                gold = gold.setScale(decimals, mode);
            if (money != null)
                money = money.setScale(decimals, mode);
            if (price != null)
                price = price.setScale(decimals, mode);
        }
    }

    public void setRelativeLowPricePct(double relativeLowPricePct) {
        this.relativeLowPricePct = relativeLowPricePct;
    }

    public double getRelativeLowPricePct() {
        return relativeLowPricePct;
    }

    public void setRelativeLowPrice(BigDecimal relativeStartPrice) {
        this.relativeLowPrice = relativeStartPrice;
    }

    public BigDecimal getRelativeLowPrice() {
        return relativeLowPrice;
    }

    public void setLowPrice(BigDecimal startPrice) {
        this.lowPrice = startPrice;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public void setRelativeHighPricePct(double relativeStopPricePct) {
        this.relativeHighPricePct = relativeStopPricePct;
    }

    public double getRelativeHighPricePct() {
        return relativeHighPricePct;
    }

    public void setRelativeHighPrice(BigDecimal relativeStopPrice) {
        this.relativeHighPrice = relativeStopPrice;
    }

    public BigDecimal getRelativeHighPrice() {
        return relativeHighPrice;
    }

    public void setHighPrice(BigDecimal stopPrice) {
        this.highPrice = stopPrice;
    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public int getPricePatternBlockCount() {
        return pricePatternBlockCount;
    }

    public int[] getPricePatternArray() {
        return pricePatternArray;
    }

    public void setTick(BigDecimal tick) {
        if (tick.equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("Tick value can not be 0");
        }
        this.tick = tick;
        this.halfTick = tick.divide(new BigDecimal("2"));
    }

    public BigDecimal getTick() {
        return tick;
    }

    public BigDecimal adjustForTick(BigDecimal value) {
        if (tick == null) {
            return value;
        }
        BigDecimal[] result = value.divideAndRemainder(tick);
        BigDecimal loBound = result[0].multiply(tick);
        return result[1].compareTo(halfTick) >= 0 ? loBound.add(tick) : loBound;
    }

    public Set<EventType> getEnabledEvents() {
        return enabledEvents;
    }

    public boolean isEnabledEvent(EventType type) {
        return enabledEvents != null && enabledEvents.contains(type);
    }

    public void setEnabledEvents(Set<EventType> enabledEvents) {
        this.enabledEvents = Collections.unmodifiableSet(new HashSet<>(enabledEvents));
    }

    public void setType(String type) {

        this.type = OrderSide.valueOf(type);
    }

    public OrderSide getType() {
        return type;
    }

    public void setActionsLimit(int actionsLimit) {
        this.actionsLimit = actionsLimit;
    }

    public int getActionsLimit() {
        return actionsLimit;
    }

    public void setNoTake(boolean noTake) {
        this.noTake = noTake;
    }

    public boolean isNoTake() {
        return noTake;
    }
}
