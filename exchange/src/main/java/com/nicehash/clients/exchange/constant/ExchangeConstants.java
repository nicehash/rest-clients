package com.nicehash.clients.exchange.constant;

import org.apache.commons.lang3.builder.ToStringStyle;

/** Constants used throughout NiceHash's API. */
public class ExchangeConstants {
  /** Default receiving window. */
  public static final long DEFAULT_RECEIVING_WINDOW = 6_000_000L;

  /**
   * Default ToStringStyle used by toString methods. Override this to change the output format of
   * the overridden toString methods. - Example ToStringStyle.JSON_STYLE
   */
  public static ToStringStyle TO_STRING_BUILDER_STYLE = ToStringStyle.SHORT_PREFIX_STYLE;
}
