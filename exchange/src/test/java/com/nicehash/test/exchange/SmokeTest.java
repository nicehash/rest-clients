package com.nicehash.test.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicehash.clients.common.AbstractClientCallback;
import com.nicehash.clients.common.ClientException;
import com.nicehash.clients.common.spi.Options;
import com.nicehash.clients.domain.Currency;
import com.nicehash.clients.exchange.ExchangeAsyncClient;
import com.nicehash.clients.exchange.ExchangeClient;
import com.nicehash.clients.exchange.ExchangeClientFactory;
import com.nicehash.clients.exchange.ExchangeWebSocketClient;
import com.nicehash.clients.exchange.domain.account.AssetBalance;
import com.nicehash.clients.exchange.domain.account.Trade;
import com.nicehash.clients.exchange.domain.event.AccountUpdateEvent;
import com.nicehash.clients.exchange.domain.event.DepthEvent;
import com.nicehash.clients.exchange.domain.event.UserDataUpdateEvent;
import com.nicehash.clients.util.cli.CryptoUtils;
import com.nicehash.clients.util.options.OptionMap;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class SmokeTest {

  @Ignore
  @Test
  public void restExample() throws Exception {
    OptionMap options =
        OptionMap.builder()
            .set(Options.BASE_URL, "https://testngapi.nicehash.com/exchange/")
            .set(Options.KEY, "<KEY>")
            .set(Options.SECRET, "<SECRET>")
            .getMap();

    ExchangeClientFactory factory = ExchangeClientFactory.newInstance(options);
    ExchangeClient client = factory.newClient();

    try {
      List<Trade> trades = client.getMyTrades("LTCBTC");
      System.out.println("Trades = " + trades);
    } catch (ClientException e) {
      System.out.println(e.getErrorBody());
    }
  }

  @Ignore
  @Test
  public void asyncExample() throws Exception {
    OptionMap options =
        OptionMap.builder()
            .set(Options.BASE_URL, "https://testngapi.nicehash.com/exchange/")
            .set(Options.KEY, "<KEY>")
            .set(Options.SECRET, "<SECRET>")
            .getMap();

    ExchangeClientFactory factory = ExchangeClientFactory.newInstance(options);
    ExchangeAsyncClient client = factory.newAsyncClient();

    try {
      client.getMyTrades(
          "LTCBTC",
          100,
          new AbstractClientCallback<>() {
            @Override
            public void onResponse(List<Trade> trades) {
              System.out.println("Trades = " + trades);
            }
          });
    } catch (ClientException e) {
      System.out.println(e.getErrorBody());
    }
  }

  @Ignore
  @Test
  public void wsExample() {
    try (ExchangeWebSocketClient client =
        ExchangeClientFactory.newWebSocketClient("https://exchangetestngapi.nicehash.com/ws")) {
      client.onDepthEvent(
          "LTCBTC",
          new AbstractClientCallback<>() {
            @Override
            public void onResponse(DepthEvent result) {
              System.out.println("Result = " + result);
            }
          });
    }
  }

  @Test
  public void testCryptoUtils() {
    CryptoUtils.hashBySegments(
        "<key>",
        "<api-key>",
        String.valueOf(System.currentTimeMillis()),
        UUID.randomUUID().toString(),
        "POST",
        "/path",
        "q=1",
        "BODY");
  }

  @Test
  public void testSubunits() {
    Assert.assertEquals(100_000_000, Currency.BTC.subunits().intValue());
    Assert.assertEquals(1_000_000, Currency.XRP.subunits().intValue());
  }

  @Test
  public void testUserDataUpdateEvent() throws Exception {
    UserDataUpdateEvent event = new UserDataUpdateEvent();
    event.setEventType(UserDataUpdateEvent.UserDataUpdateEventType.ACCOUNT_UPDATE);
    event.setEventTime(System.currentTimeMillis());

    AccountUpdateEvent accEvent = new AccountUpdateEvent();
    AssetBalance ab = new AssetBalance();
    ab.setAsset("BTC");
    ab.setFree(BigDecimal.TEN);
    ab.setLocked(BigDecimal.ONE);
    accEvent.setBalances(Collections.singletonList(ab));
    event.setAccountUpdateEvent(accEvent);

    ObjectMapper mapper = new ObjectMapper();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    mapper.writeValue(baos, event);
    System.out.println("Json = " + baos);

    event = mapper.readValue(baos.toByteArray(), UserDataUpdateEvent.class);
    Assert.assertNotNull(event);
    accEvent = event.getAccountUpdateEvent();
    Assert.assertNotNull(accEvent);
    List<AssetBalance> balances = accEvent.getBalances();
    Assert.assertNotNull(balances);
    Assert.assertFalse(balances.isEmpty());
    ab = balances.get(0);
    Assert.assertNotNull(ab);

    Assert.assertEquals("BTC", ab.getAsset());
    Assert.assertEquals(0, BigDecimal.TEN.compareTo(ab.getFree()));
    Assert.assertEquals(0, BigDecimal.ONE.compareTo(ab.getLocked()));
  }
}
