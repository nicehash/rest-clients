package com.nicehash.clients.exchange;

import com.nicehash.clients.common.Clients;
import com.nicehash.clients.common.spi.Options;
import com.nicehash.clients.exchange.impl.ExchangeWebSocketClientImpl;
import com.nicehash.clients.exchange.impl.OrderBookClientImpl;
import com.nicehash.clients.util.options.OptionMap;
import java.util.Objects;

/** A factory for creating NiceHash client objects. */
public final class ExchangeClientFactory {

  /** Options */
  private OptionMap options;

  /**
   * Instantiates a new NiceHash exchange api client factory.
   *
   * @param options the SPI options
   */
  private ExchangeClientFactory(OptionMap options) {
    this.options = options;
  }

  /**
   * Instantiates a new NiceHash exchange api client factory.
   *
   * @param apiKey the API key
   * @param secret the Secret
   */
  private ExchangeClientFactory(String apiKey, String secret) {
    this(
        OptionMap.builder()
            .set(Options.KEY, Objects.requireNonNull(apiKey, "API key must not be null!"))
            .set(Options.SECRET, Objects.requireNonNull(secret, "Secret must not be null!"))
            .getMap());
  }

  /**
   * New instance.
   *
   * @param apiKey the API key
   * @param secret the Secret
   * @return the NiceHash exchange api client factory
   */
  public static ExchangeClientFactory newInstance(String apiKey, String secret) {
    return new ExchangeClientFactory(apiKey, secret);
  }

  /**
   * New instance.
   *
   * @param options the SPI options
   * @return the NiceHash exchange api client factory
   */
  public static ExchangeClientFactory newInstance(OptionMap options) {
    return new ExchangeClientFactory(options);
  }

  /** Creates a new synchronous/blocking REST client. */
  public ExchangeClient newClient() {
    return Clients.factory().getClient(ExchangeClient.class, options);
  }

  /** Creates a new asynchronous/non-blocking REST client. */
  public ExchangeAsyncClient newAsyncClient() {
    return Clients.factory().getClient(ExchangeAsyncClient.class, options);
  }

  /** Create order book client. */
  public OrderBookClient newOrderBookClient() {
    return new OrderBookClientImpl(options);
  }

  /** Creates a new web socket client used for handling data streams. */
  public static ExchangeWebSocketClient newWebSocketClient(String baseUrl) {
    OptionMap options = OptionMap.create(Options.BASE_URL, baseUrl);
    return newWebSocketClient(options);
  }

  /** Creates a new web socket client used for handling data streams. */
  public static ExchangeWebSocketClient newWebSocketClient(OptionMap options) {
    return new ExchangeWebSocketClientImpl(options);
  }
}
