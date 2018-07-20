package com.nicehash.exchange.client.domain.market;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 24 hour price change statistics for a ticker.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TickerStatistic {

    /**
     * Ticker symbol.
     */
    private String symbol;

    /**
     * Price change during the last 24 hours.
     */
    private String priceChange;

    /**
     * Price change, in percentage, during the last 24 hours.
     */
    private String priceChangePercent;

    /**
     * Weighted average price.
     */
    private String weightedAvgPrice;

    /**
     * Previous close price.
     */
    private String prevClosePrice;

    /**
     * Bid price.
     */
    private String bidPrice;

    /**
     * Bid quantity.
     */
    private String bidQuantity;

    /**
     * Ask price.
     */
    private String askPrice;

    /**
     * Ask quantity.
     */
    private String askQuantity;

    /**
     * Open price 24 hours ago.
     */
    private String openPrice;

    /**
     * Highest price during the past 24 hours.
     */
    private String highPrice;

    /**
     * Lowest price during the past 24 hours.
     */
    private String lowPrice;

    /**
     * Total volume during the past 24 hours.
     */
    private String volume;

    /**
     * Total quote volume during the past 24 hours.
     */
    private String quoteVolume;

    /**
     * Open time.
     */
    private long openTime;

    /**
     * Close time.
     */
    private long closeTime;

    /**
     * First trade number.
     */
    private long firstNumber;

    /**
     * Last trade number.
     */
    private long lastNumber;

    /**
     * Total number of trades during the last 24 hours.
     */
    private long count;

    public String getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(String priceChange) {
        this.priceChange = priceChange;
    }

    public String getPriceChangePercent() {
        return priceChangePercent;
    }

    public void setPriceChangePercent(String priceChangePercent) {
        this.priceChangePercent = priceChangePercent;
    }

    public String getWeightedAvgPrice() {
        return weightedAvgPrice;
    }

    public void setWeightedAvgPrice(String weightedAvgPrice) {
        this.weightedAvgPrice = weightedAvgPrice;
    }

    public String getPrevClosePrice() {
        return prevClosePrice;
    }

    public void setPrevClosePrice(String prevClosePrice) {
        this.prevClosePrice = prevClosePrice;
    }

    public String getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getBidQuantity() {
        return bidQuantity;
    }

    public void setBidQuantity(String bidQuantity) {
        this.bidQuantity = bidQuantity;
    }

    public String getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(String askPrice) {
        this.askPrice = askPrice;
    }

    public String getAskQuantity() {
        return askQuantity;
    }

    public void setAskQuantity(String askQuantity) {
        this.askQuantity = askQuantity;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getQuoteVolume() {
        return quoteVolume;
    }

    public void setQuoteVolume(String quoteVolume) {
        this.quoteVolume = quoteVolume;
    }

    public long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }

    public long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(long closeTime) {
        this.closeTime = closeTime;
    }

    public long getFirstNumber() {
        return firstNumber;
    }

    public void setFirstNumber(long firstId) {
        this.firstNumber = firstId;
    }

    public long getLastNumber() {
        return lastNumber;
    }

    public void setLastNumber(long lastId) {
        this.lastNumber = lastId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("symbol", symbol)
            .append("priceChange", priceChange)
            .append("priceChangePercent", priceChangePercent)
            .append("weightedAvgPrice", weightedAvgPrice)
            .append("prevClosePrice", prevClosePrice)
            .append("bidPrice", bidPrice)
            .append("bidQty", bidQuantity)
            .append("askPrice", askPrice)
            .append("askQty", askQuantity)
            .append("openPrice", openPrice)
            .append("highPrice", highPrice)
            .append("lowPrice", lowPrice)
            .append("volume", volume)
            .append("openTime", openTime)
            .append("closeTime", closeTime)
            .append("firstNumber", firstNumber)
            .append("lastNumber", lastNumber)
            .append("count", count)
            .toString();
    }
}
