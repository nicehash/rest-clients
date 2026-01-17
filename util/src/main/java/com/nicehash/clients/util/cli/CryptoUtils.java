package com.nicehash.clients.util.cli;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptoUtils {
  private static final Logger log = LoggerFactory.getLogger(CryptoUtils.class);

  private static final String HMAC_SHA256 = "HmacSHA256";
  private static final Charset CHARSET = StandardCharsets.ISO_8859_1;

  public static String hashBySegments(
      String key,
      String apiKey,
      String time,
      String nonce,
      String method,
      String encodedPath,
      String query,
      String bodyStr) {

    List<byte[]> segments =
        Arrays.asList(
            apiKey.getBytes(CHARSET),
            time.getBytes(CHARSET),
            nonce.getBytes(CHARSET),
            null, // unused field
            null, // unused field
            null, // unused field
            method.getBytes(CHARSET),
            encodedPath == null ? null : encodedPath.getBytes(CHARSET),
            query == null ? null : query.getBytes(CHARSET));

    if (bodyStr != null && bodyStr.length() > 0) {
      segments = new ArrayList<>(segments);
      segments.add(bodyStr.getBytes(StandardCharsets.UTF_8));
    }

    return hmacSha256BySegments(key, segments);
  }

  private static String hmacSha256BySegments(String key, List<byte[]> segments) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      Mac mac = Mac.getInstance(HMAC_SHA256);
      SecretKeySpec secret_key =
          new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
      mac.init(secret_key);
      boolean first = true;
      for (byte[] segment : segments) {
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

      log.info("Hash input: [{}]", baos.toString(CHARSET.name()));
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
