package com.nicehash.external.utils;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.logging.Level;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * A request interceptor that injects the API Key Header into requests, and signs messages, whenever required.
 */
public class AuthenticationInterceptor extends AbstractAuthenticationInterceptor {

    public AuthenticationInterceptor(String apiKey, String secret) {
        super(apiKey, secret);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder newRequestBuilder = original.newBuilder();

        boolean isApiKeyRequired = original.header(HeaderConstants.ENDPOINT_SECURITY_TYPE_APIKEY) != null;
        boolean isSignatureRequired = original.header(HeaderConstants.ENDPOINT_SECURITY_TYPE_SIGNED) != null;
        newRequestBuilder.removeHeader(HeaderConstants.ENDPOINT_SECURITY_TYPE_SIGNED).removeHeader(HeaderConstants.ENDPOINT_SECURITY_TYPE_SIGNED);

        // Endpoint requires sending a valid API-KEY
        if (isApiKeyRequired || isSignatureRequired) {
            String time = String.valueOf(System.currentTimeMillis());
            String nonce = UUID.randomUUID().toString();
            newRequestBuilder.addHeader(HeaderConstants.TIME_HEADER, time);
            newRequestBuilder.addHeader(HeaderConstants.NONCE_HEADER, nonce);

            String bodyStr = "";
            String method = original.method();
            if ("POST".equals(method) || "PUT".equals(method)) {
                RequestBody body = original.body();
                if (body != null && body.contentType() != null) {
                    bodyStr = bodyToString(body);
                    if (bodyStr.length() > 0) {
                        newRequestBuilder.method(method, RequestBody.create(body.contentType(), bodyStr.getBytes("utf-8")));
                    }
                }
            }

            String content =
                apiKey +
                time +
                nonce +
                original.method() +
                original.url().encodedPath() +
                ((original.url() == null || original.url().query() == null || original.url().query().isEmpty()) ? "" : "?" + original.url().query()) +
                bodyStr;

            String digest = hmacSha256(secret, content);
            newRequestBuilder.addHeader(HeaderConstants.AUTH_HEADER, apiKey + ":" + digest);
        }

        // Build new request after adding the necessary authentication information
        Request newRequest = newRequestBuilder.build();
        return chain.proceed(newRequest);
    }

    private String hmacSha256(String key, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException var4) {
            log.log(Level.SEVERE, "hmacSha256: exception " + var4.getMessage(), var4);
            throw new RuntimeException("Cannot create hmacSHA256");
        }
    }
}