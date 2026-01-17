package com.nicehash.clients.common.gen;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class ByteArrayConverterFactory extends Converter.Factory {
  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(
      Type type, Annotation[] annotations, Retrofit retrofit) {
    return new Converter<ResponseBody, byte[]>() {
      @Override
      public byte[] convert(ResponseBody body) throws IOException {
        return body.byteStream().readAllBytes();
      }
    };
  }
}
