package com.nicehash.exchange.client.domain.account;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

/**
 * Represents an executed trade.
 */
public class Trade extends TradeHistoryItem {
    /**
     * Commission.
     */
    private BigDecimal commission;

    /**
     * Asset on which commission is taken
     */
    private String commissionAsset;

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getCommissionAsset() {
        return commissionAsset;
    }

    public void setCommissionAsset(String commissionAsset) {
        this.commissionAsset = commissionAsset;
    }

    @Override
    protected ToStringBuilder toStringBuilder() {
        return super.toStringBuilder()
                    .append("commission", commission)
                    .append("commissionAsset", commissionAsset);
    }
}
