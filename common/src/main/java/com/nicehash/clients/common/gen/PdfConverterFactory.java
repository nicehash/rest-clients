package com.nicehash.clients.common.gen;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.ResponseBody;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class PdfConverterFactory extends Converter.Factory {

    private final JacksonConverterFactory jacksonConverterFactory;

    public PdfConverterFactory() {
        this.jacksonConverterFactory = JacksonConverterFactory.create();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            Type bodyType = parameterizedType.getActualTypeArguments()[0];

            if (rawType == ResponseEntity.class && bodyType == Resource.class) {
                return new Converter<ResponseBody, ResponseEntity<?>>() {
                    @Override
                    public ResponseEntity<?> convert(ResponseBody body) throws IOException {
                        byte[] bytes = body.bytes();
                        Resource resource = new ByteArrayResource(bytes);
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentLength(bytes.length);
                        headers.add("Content-Type", String.valueOf(body.contentType()));

                        return new ResponseEntity<>(resource, headers, org.springframework.http.HttpStatus.OK);
                    }
                };
            }
        }

        return jacksonConverterFactory.responseBodyConverter(type, annotations, retrofit);
    }

}
