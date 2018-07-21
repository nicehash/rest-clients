package com.nicehash.tools.nhcurl;

import com.nicehash.utils.cli.CliUtils;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class CurlMain {

    String endpointUrl = CliUtils.getNicehashUrl("https://testngapi.nicehash.com/exchange");

    String key = CliUtils.getApiKey(null);
    String secret = CliUtils.getApiSecret(null);

    String url;
    String method;

    StringBuilder body = new StringBuilder();
    boolean stdoutMode = true;
    boolean stderrMode = false;
    boolean noBuffer = false;

    Map<String, String> replacements = new HashMap<>();

    CurlMain() {
        replacements.put("default.recv.window", "6000000");
        replacements.put("timestamp", String.valueOf(System.currentTimeMillis()));
        replacements.put("ws.key", generateWsKey());
    }

    public static void main(String [] args) throws Exception {
        new CurlMain().execute(args);
    }

    public void execute(String [] args) throws Exception {

/*
See `man curl` for more

        -F, --form ... POST
        -d --data --data-raw --data-binary --data-ascii  --data-urlencode ... POST, application/x-www-form-urlencoded (This option overrides -F, --form and -I, --head and --upload)
        -I, --head ... HEAD
        -G, --get ... GET ... any data is moved into query string
        -X, --request ... custom METHOD as specified (PUT, DELETE ...)
*/

        checkRequiredSettings();

        List<String> argls = new LinkedList<String>();

        for (int pass = 0; pass < 2; pass++) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];

                switch (arg) {
                    case "-F":
                    case "--form": {
                        if (pass == 0) {
                            continue;
                        }
                        method = method != null ? method : "POST";
                        String keyval = interpolate(args[++i]);
                        addData(arg, keyval, true);
                        argls.add(arg);
                        argls.add(keyval);
                        break;
                    }
                    case "-d":
                    case "--data":
                    case "--data-raw":
                    case "--data-binary":
                    case "--data-ascii":
                    case "data-urlencode": {
                        if (pass == 0) {
                            continue;
                        }
                        method = method != null ? method : "POST";
                        String data = interpolate(args[++i]);
                        addData(data, null, false);
                        argls.add(arg);
                        argls.add(data);
                        break;
                    }
                    case "-I":
                    case "--head": {
                        if (pass == 0) {
                            method = "HEAD";
                            continue;
                        }
                        argls.add(arg);
                        break;
                    }
                    case "-G":
                    case "--get": {
                        if (pass == 0) {
                            method = "GET";
                            continue;
                        }
                        argls.add(arg);
                        break;
                    }
                    case "-X":
                    case "--request": {
                        if (pass == 0) {
                            method = args[++i];
                            continue;
                        }
                        argls.add(arg);
                        argls.add(args[++i]);
                        break;
                    }
                    default: {
                        if (pass == 0) {
                            if ("-N".equals(arg) || "--no-buffer".equals(arg)) {
                                noBuffer = true;
                            }
                            continue;
                        }
                        if (arg.startsWith("-") || arg.startsWith("--")) {
                            argls.add(arg);
                            checkStdoutMode(arg);
                            int skip = getArgsToSkip(arg);
                            for (int j = 0; j < skip; j++) {
                                argls.add(interpolate(args[++i]));
                            }
                        } else {
                            url = interpolate(arg);
                            argls.add(url);
                            if (method == null) {
                                method = "GET";
                            }
                        }
                    }
                }
            }
        }

        StringBuilder cmd = new StringBuilder("curl");
        argls.forEach((a) -> cmd.append(" \"").append(a).append("\""));
        digestArgs(url).forEach((a) -> cmd.append(" \"").append(a).append("\""));

        //System.out.println(cmd);

        Process p = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", cmd.toString()});

        Thread t1 = null;
        if (stdoutMode || !stderrMode) {
            t1 = new Thread(() -> copyBytes(p.getInputStream(), System.out, noBuffer));
            t1.start();
        }

        ByteArrayOutputStream memErr = new ByteArrayOutputStream();

        Thread t2 = new Thread(() -> {
            if (!stdoutMode || stderrMode) {
                copyBytes(p.getErrorStream(), System.err, noBuffer);
            } else {
                copyBytes(p.getErrorStream(), memErr, noBuffer);
            }
        });
        t2.start();

        int exit = p.waitFor();

        if (t1 != null) {
            t1.join();
        }
        t2.join();

        //if (exit != 0) {
        //    copyBytes(new ByteArrayInputStream(memErr.toByteArray()), System.err);
        //}

        System.exit(exit);
    }

    private void checkStdoutMode(String arg) {
        switch (arg) {
            case "-o":
            case "-O": {
                stdoutMode = false;
                break;
            }
            case "-i":
            case "--include":
            case "-v": {
                stderrMode = true;
                break;
            }
        }
    }

    private static int getArgsToSkip(String arg) {
        switch (arg) {
            case "-v":
            case "--verbose":
            case "-i":
            case "--include":
            case "-O":
            case "-N":
            case "--no-buffer":
                return 0;
            default:
                return 1;
        }
    }

    void checkRequiredSettings() {
        if (key == null) {
            throw new RuntimeException("API KEY not set");
        }

        if (secret == null) {
            throw new RuntimeException("API SECRET not set");
        }
    }

    static void copyBytes(InputStream is, OutputStream os, boolean flush) {
        int c;
        try {
            while ((c = is.read()) != -1) {
                os.write(c);
                if (flush) {
                    os.flush();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("I/O error", e);
        } finally {
            try {
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String interpolate(String arg) {
        for (Map.Entry<String, String> ent: replacements.entrySet()) {
            arg = arg.replaceAll("\\Q${" + ent.getKey() + "}\\E", ent.getValue());
        }
        return arg;
    }

    private void addData(String arg, String keyval, boolean encode) {
        int idx = keyval.indexOf("=");
        String name = idx == -1 ? keyval : keyval.substring(0, idx);
        String value = idx == -1 ? null : keyval.substring(idx + 1);
        try {
            addBody(name, encode && value != null ? URLEncoder.encode(value, "utf-8") : value);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to process [" + arg + " " + keyval + "]");
        }
    }

    private void addBody(String name, String value) {
        if (body.length() > 0) {
            body.append("&");
        }
        body.append(name);
        if (value != null) {
            body.append("=").append(value);
        }
    }


    private List<String> digestArgs(String url) {

        List<String> args = new LinkedList<>();
        URI uri;
        try {
            uri = new URI(url);

            String path = uri.getPath();
            String query = uri.getQuery();

            String time = String.valueOf(System.currentTimeMillis());
            String nonce = UUID.randomUUID().toString();

            String input = key +
                time +
                nonce +
                method +
                (path == null ? "" : path) +
                (query == null || "".equals(query) ? "" : "?" + query) +
                body.toString();

            String digest = hmacSha256(secret, input);

            args.add("-H");
            args.add("X-Time: " + time);
            args.add("-H");
            args.add("X-Nonce: " + nonce);
            args.add("-H");
            args.add("X-Auth: " + key + ":" + digest);

        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate crypto signature: ", e);
        }

        return args;
    }

    private String hmacSha256(String key, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Cannot create hmacSHA256", e);
        }
    }

    private String generateWsKey() {
        byte[] nonce = new byte[16];
        new SecureRandom().nextBytes(nonce);
        return Base64.getEncoder().encodeToString(nonce);
    }
}
