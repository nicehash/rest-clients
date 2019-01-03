package com.nicehash.clients.exchange.domain.event;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Custom serializer for a User Data stream event, since the API can return two different responses in this stream.
 *
 * @see UserDataUpdateEvent
 */
public class UserDataUpdateEventSerializer extends JsonSerializer<UserDataUpdateEvent> {
    @Override
    public void serialize(UserDataUpdateEvent event, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        AccountUpdateEvent accountUpdateEvent = event.getAccountUpdateEvent();
        OrderTradeUpdateEvent orderTradeUpdateEvent = event.getOrderTradeUpdateEvent();

        if (accountUpdateEvent == null && orderTradeUpdateEvent == null) {
            throw new IllegalArgumentException("Both sub-events are null!");
        }

        if (accountUpdateEvent != null && orderTradeUpdateEvent != null) {
            throw new IllegalArgumentException("Both sub-events cannot be set, only one!");
        }

        if (accountUpdateEvent != null) {
            accountUpdateEvent.setEventType(UserDataUpdateEvent.UserDataUpdateEventType.ACCOUNT_UPDATE.getEventTypeId());
            accountUpdateEvent.setEventTime(event.getEventTime());
            gen.writeObject(accountUpdateEvent);
        } else {
            orderTradeUpdateEvent.setEventType(UserDataUpdateEvent.UserDataUpdateEventType.ORDER_TRADE_UPDATE.getEventTypeId());
            orderTradeUpdateEvent.setEventTime(event.getEventTime());
            gen.writeObject(orderTradeUpdateEvent);
        }
    }
}
