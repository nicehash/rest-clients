package com.nicehash.clients.exchange.domain.account.request;

import com.nicehash.clients.exchange.constant.ExchangeConstants;
import com.nicehash.clients.exchange.domain.SortDirection;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Base request parameters for order-related methods.
 */
public class OrderRequest {

    private final String symbol;

    private SortDirection sortDirection;

    private Long recvWindow;

    private Long timestamp;

    public OrderRequest(String symbol) {
        this.symbol = symbol;
        this.timestamp = System.currentTimeMillis();
        this.recvWindow = ExchangeConstants.DEFAULT_RECEIVING_WINDOW;
    }

    public String getSymbol() {
        return symbol;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public OrderRequest direction(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
        return this;
    }

    public Long getRecvWindow() {
        return recvWindow;
    }

    public OrderRequest recvWindow(Long recvWindow) {
        this.recvWindow = recvWindow;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public OrderRequest timestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ExchangeConstants.TO_STRING_BUILDER_STYLE)
            .append("symbol", symbol)
            .append("sortDirection", sortDirection)
            .append("recvWindow", recvWindow)
            .append("timestamp", timestamp)
            .toString();
    }
}
