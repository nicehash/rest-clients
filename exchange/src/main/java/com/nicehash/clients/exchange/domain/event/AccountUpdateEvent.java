package com.nicehash.clients.exchange.domain.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nicehash.clients.exchange.constant.ExchangeConstants;
import com.nicehash.clients.exchange.domain.account.AssetBalance;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Account update event which will reflect the current position/balances of the account.
 *
 * <p>This event is embedded as part of a user data update event.
 *
 * @see UserDataUpdateEvent
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountUpdateEvent {

  @JsonProperty("e")
  private String eventType;

  @JsonProperty("E")
  private long eventTime;

  @JsonProperty("B")
  @JsonSerialize(contentUsing = AssetBalanceSerializer.class)
  @JsonDeserialize(contentUsing = AssetBalanceDeserializer.class)
  private List<AssetBalance> balances;

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public long getEventTime() {
    return eventTime;
  }

  public void setEventTime(long eventTime) {
    this.eventTime = eventTime;
  }

  public List<AssetBalance> getBalances() {
    return balances;
  }

  public void setBalances(List<AssetBalance> balances) {
    this.balances = balances;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ExchangeConstants.TO_STRING_BUILDER_STYLE)
        .append("eventType", eventType)
        .append("eventTime", eventTime)
        .append("balances", balances)
        .toString();
  }
}
