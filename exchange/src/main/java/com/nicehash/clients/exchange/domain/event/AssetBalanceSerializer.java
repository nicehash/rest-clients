package com.nicehash.clients.exchange.domain.event;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.nicehash.clients.exchange.domain.account.AssetBalance;

import java.io.IOException;

/**
 * Custom serializer for an AssetBalance, since the streaming API returns an object in the format {"a":"symbol","f":"free","l":"locked"},
 * which is different than the format used in the REST API.
 */
public class AssetBalanceSerializer extends JsonSerializer<AssetBalance> {

    @Override
    public void serialize(AssetBalance value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeFieldName("a");
        gen.writeString(value.getAsset());

        gen.writeFieldName("f");
        gen.writeString(value.getFree().toString());

        gen.writeFieldName("l");
        gen.writeString(value.getLocked().toString());

        gen.writeEndObject();
    }
}
