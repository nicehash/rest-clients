package com.nicehash.exchange.client.domain.market;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Custom deserializer for an Candlestick.
 */
public class CandlestickDeserializer extends JsonDeserializer<Candlestick> {

    @Override
    public Candlestick deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);

        Long openTime = node.get(0).longValue();
        String open = node.get(1).textValue();
        String high = node.get(2).textValue();
        String low = node.get(3).textValue();
        String close = node.get(4).textValue();
        String volume = node.get(5).textValue();
        Long closeTime = node.get(6).longValue();
        String quoteAssetVolume = node.get(7).textValue();
        Long numberOfTrades = node.get(8).longValue();
        String takerBuyBaseAssetVolume = node.get(9).textValue();
        String takerBuyQuoteAssetVolume = node.get(10).textValue();

        Candlestick cs = new Candlestick();
        cs.setOpenTime(openTime);
        cs.setOpen(open);
        cs.setHigh(high);
        cs.setLow(low);
        cs.setClose(close);
        cs.setVolume(volume);
        cs.setCloseTime(closeTime);
        cs.setQuoteAssetVolume(quoteAssetVolume);
        cs.setNumberOfTrades(numberOfTrades);
        cs.setTakerBuyBaseAssetVolume(takerBuyBaseAssetVolume);
        cs.setTakerBuyQuoteAssetVolume(takerBuyQuoteAssetVolume);
        return cs;
    }
}
