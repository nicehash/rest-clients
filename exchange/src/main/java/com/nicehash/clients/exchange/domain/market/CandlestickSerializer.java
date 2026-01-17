package com.nicehash.clients.exchange.domain.market;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/** Custom serializer for an Candlestick. */
public class CandlestickSerializer extends JsonSerializer<Candlestick> {

  @Override
  public void serialize(Candlestick cs, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    gen.writeStartArray();
    gen.writeNumber(cs.getOpenTime());
    gen.writeString(cs.getOpen());
    gen.writeString(cs.getHigh());
    gen.writeString(cs.getLow());
    gen.writeString(cs.getClose());
    gen.writeString(cs.getVolume());
    gen.writeNumber(cs.getCloseTime());
    gen.writeString(cs.getQuoteAssetVolume());
    gen.writeNumber(cs.getNumberOfTrades());
    gen.writeString(cs.getTakerBuyBaseAssetVolume());
    gen.writeString(cs.getTakerBuyQuoteAssetVolume());
    gen.writeEndArray();
  }
}
