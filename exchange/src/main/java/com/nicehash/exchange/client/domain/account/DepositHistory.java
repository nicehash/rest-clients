package com.nicehash.exchange.client.domain.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nicehash.exchange.client.constant.ExchangeConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * History of account deposits.
 *
 * @see Deposit
 */
public class DepositHistory {

    @JsonProperty("depositList")
    private List<Deposit> depositList;

    private boolean success;

    public List<Deposit> getDepositList() {
        return depositList;
    }

    public void setDepositList(List<Deposit> depositList) {
        this.depositList = depositList;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ExchangeConstants.TO_STRING_BUILDER_STYLE)
            .append("depositList", depositList)
            .append("success", success)
            .toString();
    }
}
