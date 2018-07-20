package com.nicehash.exchange.client.domain.market;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nicehash.exchange.client.constant.ExchangeConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

/**
 * An order book entry consisting of price and quantity.
 */
@JsonDeserialize(using = OrderBookEntryDeserializer.class)
@JsonSerialize(using = OrderBookEntrySerializer.class)
public class OrderBookEntry {

    private BigDecimal price;
    private BigDecimal qty;

    public OrderBookEntry() {
    }

    public OrderBookEntry(BigDecimal price, BigDecimal qty) {
        this.price = price;
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ExchangeConstants.TO_STRING_BUILDER_STYLE)
            .append("price", price)
            .append("qty", qty)
            .toString();
    }
}
