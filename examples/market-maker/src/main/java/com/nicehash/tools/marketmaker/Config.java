package com.nicehash.tools.marketmaker;

import com.nicehash.exchange.client.domain.account.AssetBalance;
import com.nicehash.utils.cli.Market;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Config {

    private Market market;
    private BigDecimal gold = BigDecimal.ZERO;
    private BigDecimal money = BigDecimal.ZERO;
    private double spread;
    private int buckets;
    private BigDecimal fallbackPrice = BigDecimal.ZERO;
    private BigDecimal priceOverride = BigDecimal.ZERO;
    private double maxOrdersPerSec = 20;
    private boolean ignoreLimits;
    private AssetBalance currentGoldBalance;
    private AssetBalance currentMoneyBalance;

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

    public double getSpread() {
        return spread;
    }

    public void setSpread(double spread) {
        this.spread = spread;
    }

    public int getBuckets() {
        return buckets;
    }

    public void setBuckets(int buckets) {
        this.buckets = buckets;
    }

    public BigDecimal getFallbackPrice() {
        return fallbackPrice;
    }

    public void setFallbackPrice(BigDecimal fallbackPrice) {
        this.fallbackPrice = fallbackPrice;
    }

    public BigDecimal getPriceOverride() {
        return priceOverride;
    }

    public void setPriceOverride(BigDecimal priceOverride) {
        this.priceOverride = priceOverride;
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


    public AssetBalance getCurrentGoldBalance() {
        return currentGoldBalance;
    }

    public void setCurrentGoldBalance(AssetBalance currentGoldBalance) {
        this.currentGoldBalance = currentGoldBalance;
    }

    public AssetBalance getCurrentMoneyBalance() {
        return currentMoneyBalance;
    }

    public void setCurrentMoneyBalance(AssetBalance currentMoneyBalance) {
        this.currentMoneyBalance = currentMoneyBalance;
    }

    public void applyPrecisionLimits(int decimals, RoundingMode mode) {
        if (gold != null)
            gold = gold.setScale(decimals, mode);
        if (money != null)
            money = money.setScale(decimals, mode);
        if (fallbackPrice != null)
            fallbackPrice = fallbackPrice.setScale(decimals, mode);
        if (priceOverride != null)
            priceOverride = priceOverride.setScale(decimals, mode);
    }

}
