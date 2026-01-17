package com.nicehash.clients.exchange.domain.account;

import com.nicehash.clients.exchange.constant.ExchangeConstants;
import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * An asset balance in an Account.
 *
 * @see Account
 */
public class AssetBalance {

  /** Asset symbol. */
  private String asset;

  /** Available balance. */
  private BigDecimal free;

  /** Locked by open orders. */
  private BigDecimal locked;

  public String getAsset() {
    return asset;
  }

  public void setAsset(String asset) {
    this.asset = asset;
  }

  public BigDecimal getFree() {
    return free;
  }

  public void setFree(BigDecimal free) {
    this.free = free;
  }

  public BigDecimal getLocked() {
    return locked;
  }

  public void setLocked(BigDecimal locked) {
    this.locked = locked;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ExchangeConstants.TO_STRING_BUILDER_STYLE)
        .append("asset", asset)
        .append("free", free)
        .append("locked", locked)
        .toString();
  }
}
