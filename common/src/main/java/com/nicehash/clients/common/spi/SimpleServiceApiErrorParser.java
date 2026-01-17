package com.nicehash.clients.common.spi;

import com.nicehash.clients.common.ClientException;
import java.io.IOException;
import java.io.InputStream;
import retrofit2.Response;

public class SimpleServiceApiErrorParser implements ServiceApiErrorParser {
  public static final ServiceApiErrorParser INSTANCE = new SimpleServiceApiErrorParser();

  private SimpleServiceApiErrorParser() {}

  @Override
  public ServiceApiError parse(Response response) {
    return new ServiceApiError() {
      @Override
      public int getCode() {
        return response.code();
      }

      @Override
      public String getMessage() {
        return response.message();
      }

      @Override
      public InputStream getBodyAsStream() {
        return response.errorBody().byteStream();
      }

      @Override
      public String getBodyAsString() throws IOException {
        return response.errorBody().string();
      }

      @Override
      public RuntimeException toException() {
        return new ClientException(this);
      }
    };
  }
}
