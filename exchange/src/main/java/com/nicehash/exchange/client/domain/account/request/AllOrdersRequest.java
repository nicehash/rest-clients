package com.nicehash.exchange.client.domain.account.request;

import com.nicehash.common.domain.OrderRelOp;

/**
 * A specialized order request with additional filters.
 */
public class AllOrdersRequest extends OrderRequest {

    private static final Integer DEFAULT_LIMIT = 500;

    private OrderRelOp relationalOp;

    private Long submitNumber;

    private Long submitTime;

    private Boolean terminated;

    private Integer limit;

    public AllOrdersRequest(String symbol) {
        super(symbol);
        this.limit = DEFAULT_LIMIT;
    }

    public OrderRelOp getRelationalOp() {
        return relationalOp;
    }

    public AllOrdersRequest relationalOp(OrderRelOp relationalOp) {
        this.relationalOp = relationalOp;
        return this;
    }

    public Long getSubmitNumber() {
        return submitNumber;
    }

    public AllOrdersRequest submitNumber(Long submitNumber) {
        this.submitNumber = submitNumber;
        return this;
    }

    public Long getSubmitTime() {
        return submitTime;
    }

    public AllOrdersRequest submitTime(Long submitTime) {
        this.submitTime = submitTime;
        return this;
    }

    public Boolean getTerminated() {
        return terminated;
    }

    public AllOrdersRequest terminated(Boolean terminated) {
        this.terminated = terminated;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public AllOrdersRequest limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public String toString() {
        return "AllOrdersRequest{" +
               "relationalOp=" + relationalOp +
               ", submitNumber=" + submitNumber +
               ", submitTime=" + submitTime +
               ", terminated=" + terminated +
               ", limit=" + limit +
               '}';
    }
}
