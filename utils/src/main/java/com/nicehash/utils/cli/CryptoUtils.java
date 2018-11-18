package com.nicehash.utils.cli;

import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String UTF_8 = "UTF-8";

    public static String hmacSha256(String key, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(UTF_8), HMAC_SHA256);
            sha256_HMAC.init(secret_key);
            return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes(UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("Cannot create HmacSHA256", e);
        }
    }

    public static String hashBySegments(String key, String apiKey, String time, String nonce, String method, String encodedPath, String query, String bodyStr) {

        List<byte []> segments = Arrays.asList(
            apiKey.getBytes(StandardCharsets.ISO_8859_1),
            time.getBytes(StandardCharsets.ISO_8859_1),
            nonce.getBytes(StandardCharsets.ISO_8859_1),
            null,  // unused field
            null,  // unused field
            null,  // unused field
            method.getBytes(StandardCharsets.ISO_8859_1),
            encodedPath == null ? null : encodedPath.getBytes(StandardCharsets.ISO_8859_1),
            query == null ? null : query.getBytes(StandardCharsets.ISO_8859_1));

        if (bodyStr != null && bodyStr.length() > 0) {
            segments.add(bodyStr.getBytes(StandardCharsets.UTF_8));
        }

        return hmacSha256BySegments(key, segments);
    }

    private static String hmacSha256BySegments(String key, List<byte []> segments) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(UTF_8), HMAC_SHA256);
            mac.init(secret_key);
            boolean first = true;
            for (byte [] segment: segments) {
                if (!first) {
                    mac.update((byte) 0);
                    baos.write((byte) 0);
                } else {
                    first = false;
                }
                if (segment != null) {
                    mac.update(segment);
                    baos.write(segment);
                }
            }

            // System.out.println("hash input: [" + baos.toString("iso-8859-1") + "]");
            return Hex.encodeHexString(mac.doFinal());
        } catch (Exception e) {
            throw new RuntimeException("Cannot create HmacSHA256", e);
        }
    }

    public static String generateWsKey() {
        byte[] nonce = new byte[16];
        new SecureRandom().nextBytes(nonce);
        return Base64.getEncoder().encodeToString(nonce);
    }
}
