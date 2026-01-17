package com.nicehash.clients.common.apierror;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicehash.clients.common.ClientException;
import java.io.IOException;

public class ApiErrorParser {
  private static final ObjectMapper mapper = new ObjectMapper();

  public static String getErrorBodyRaw(ClientException ce) {
    try {
      return ce.getErrorBody();
    } catch (IOException ignored) {
      return null;
    }
  }

  public static ApiErrorData getErrorBody(ClientException ce) {
    try {
      String errorBody = ce.getErrorBody();
      return mapper.readValue(errorBody, ApiErrorData.class);
    } catch (IOException ignored) {
      return new ApiErrorData();
    }
  }
}
