package com.nicehash.exchange.client.domain.account.request;

import com.nicehash.exchange.client.constant.ExchangeConstants;
import com.nicehash.exchange.client.domain.RelationalOp;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A specialized order request with additional filters.
 */
public class AllOrdersRequest extends OrderRequest {

    private static final Integer DEFAULT_LIMIT = 500;

    private RelationalOp relationalOp;

    private Long submitNumber;

    private Integer limit;

    public AllOrdersRequest(String symbol) {
        super(symbol);
        this.limit = DEFAULT_LIMIT;
    }

    public RelationalOp getRelationalOp() {
        return relationalOp;
    }

    public AllOrdersRequest relationalOp(RelationalOp relationalOp) {
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
    public Integer getLimit() {
        return limit;
    }

    public AllOrdersRequest limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ExchangeConstants.TO_STRING_BUILDER_STYLE)
            .append(super.toString())
            .append("relationalOp", relationalOp)
            .append("submitNumber", submitNumber)
            .append("limit", limit)
            .toString();
    }
}
