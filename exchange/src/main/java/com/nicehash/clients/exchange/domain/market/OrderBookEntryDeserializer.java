package com.nicehash.clients.exchange.domain.market;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Custom deserializer for an OrderBookEntry, since the API returns an array in the format [ price, qty, [] ].
 */
public class OrderBookEntryDeserializer extends JsonDeserializer<OrderBookEntry> {

    @Override
    public OrderBookEntry deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);
        final BigDecimal price = node.get(0).decimalValue();
        final BigDecimal qty = node.get(1).decimalValue();

        OrderBookEntry orderBookEntry = new OrderBookEntry();
        orderBookEntry.setPrice(price);
        orderBookEntry.setQty(qty);
        return orderBookEntry;
    }
}
