package com.nicehash.clients.exchange.domain.account;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Fee status.
*/
public class FeeStatus {
    private BigDecimal coefficient;
    private BigDecimal sum;
    private Map<BigDecimal, BigDecimal> limits;
    private String sumCurrency;

    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public Map<BigDecimal, BigDecimal> getLimits() {
        return limits;
    }

    public void setLimits(Map<BigDecimal, BigDecimal> limits) {
        this.limits = limits;
    }

    public String getSumCurrency() {
        return sumCurrency;
    }

    public void setSumCurrency(String sumCurrency) {
        this.sumCurrency = sumCurrency;
    }
}
