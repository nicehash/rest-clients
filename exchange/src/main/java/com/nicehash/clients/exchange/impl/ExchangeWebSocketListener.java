package com.nicehash.clients.exchange.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicehash.clients.common.ClientCallback;
import com.nicehash.clients.common.ClientException;
import java.io.IOException;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/** Exchange API WebSocket listener. */
public class ExchangeWebSocketListener<T> extends WebSocketListener {

  private ClientCallback<T> callback;
  private Class<T> eventClass;
  private TypeReference<T> eventTypeReference;

  private ObjectMapper mapper = new ObjectMapper();

  private volatile boolean closing = false;

  public ExchangeWebSocketListener(ClientCallback<T> callback, Class<T> eventClass) {
    this.callback = callback;
    this.eventClass = eventClass;
  }

  public ExchangeWebSocketListener(ClientCallback<T> callback) {
    this.callback = callback;
    this.eventTypeReference = new TypeReference<T>() {};
  }

  @Override
  public void onMessage(WebSocket webSocket, String text) {
    try {
      T event;
      if (eventClass == null) {
        event = mapper.readValue(text, eventTypeReference);
      } else {
        event = mapper.readValue(text, eventClass);
      }
      callback.onResponse(event);
    } catch (IOException e) {
      throw new ClientException("", e);
    }
  }

  @Override
  public void onClosing(final WebSocket webSocket, final int code, final String reason) {
    closing = true;
  }

  @Override
  public void onFailure(WebSocket webSocket, Throwable t, Response response) {
    if (!closing) {
      callback.onFailure(t);
    }
  }
}
