package com.nicehash.clients.common.util;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.nicehash.clients.util.cli.CryptoUtils.hashBySegments;

import java.io.IOException;
import java.util.UUID;

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

            String digest = hashBySegments(secret, apiKey, time, nonce, method, original.url().encodedPath(), original.url().query(), bodyStr);

            newRequestBuilder.addHeader(HeaderConstants.AUTH_HEADER, apiKey + ":" + digest);
/*
            System.out.println("Message Digest Inputs: [\n  apiKey: [" + apiKey + "]\n  time: [" + time + "]\n  nonce: [" + nonce +
                               "]\n  method: [" + original.method() + "]\n  path: [" + original.url().encodedPath() + "]\n  query: [" +
                               ((original.url() == null || original.url().query() == null || original.url().query().isEmpty()) ? "" : "?" + original.url().query()) +
                               "]\n  body: [" + bodyStr + "]\n]");
            System.out.println("Message Digest Raw Input: [" + content + "]");
            System.out.println("Secret: [" + secret + "]");
            System.out.println("Message Digest (SHA-256): [" + digest + "]");
            System.out.println(HeaderConstants.AUTH_HEADER + " header: [" + apiKey + ":" + digest + "]");
*/
        }

        // Build new request after adding the necessary authentication information
        Request newRequest = newRequestBuilder.build();
        return chain.proceed(newRequest);
    }
}