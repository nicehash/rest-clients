package com.nicehash.test.exchange;

import com.nicehash.exchange.client.ExchangeAsyncClient;
import com.nicehash.exchange.client.ExchangeClient;
import com.nicehash.exchange.client.ExchangeClientFactory;
import com.nicehash.exchange.client.ExchangeWebSocketClient;
import com.nicehash.exchange.client.domain.account.Trade;
import com.nicehash.exchange.client.domain.event.DepthEvent;
import com.nicehash.external.AbstractClientCallback;
import com.nicehash.external.ClientException;
import com.nicehash.external.spi.Options;
import com.nicehash.utils.options.OptionMap;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * @author Ales Justin
 */
public class SmokeTest {

    @Ignore
    @Test
    public void restExample() throws Exception {
        OptionMap options = OptionMap.builder()
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
        OptionMap options = OptionMap.builder()
                                     .set(Options.BASE_URL, "https://testngapi.nicehash.com/exchange/")
                                     .set(Options.KEY, "<KEY>")
                                     .set(Options.SECRET, "<SECRET>")
                                     .getMap();

        ExchangeClientFactory factory = ExchangeClientFactory.newInstance(options);
        ExchangeAsyncClient client = factory.newAsyncClient();

        try {
            client.getMyTrades("LTCBTC", 100, new AbstractClientCallback<List<Trade>>() {
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
        try (ExchangeWebSocketClient client = ExchangeClientFactory.newWebSocketClient("https://exchangetestngapi.nicehash.com/ws")) {
            client.onDepthEvent("LTCBTC", new AbstractClientCallback<DepthEvent>() {
                @Override
                public void onResponse(DepthEvent result) {
                    System.out.println("Result = " + result);
                }
            });
        }
    }
}
