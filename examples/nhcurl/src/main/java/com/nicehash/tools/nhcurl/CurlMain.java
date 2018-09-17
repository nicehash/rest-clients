package com.nicehash.tools.nhcurl;

import com.nicehash.utils.cli.CliUtils;

import static com.nicehash.utils.cli.CryptoUtils.generateWsKey;
import static com.nicehash.utils.cli.CryptoUtils.hmacSha256;
import static com.nicehash.utils.cli.IoUtils.copyBytes;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CurlMain {

    static final String API_KEY = CliUtils.getApiKey(null);
    static final String API_SECRET = CliUtils.getApiSecret(null);

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

    void execute(String [] args) throws Exception {

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
                        }
                    }
                }
            }
        }

        if (url == null) {
            System.out.println("Usage: nhcurl URL [OPTIONS]");
            System.exit(1);
        }

        if (method == null) {
            method = "GET";
        }

        StringBuilder cmd = new StringBuilder("curl");
        argls.forEach((a) -> cmd.append(" \'").append(a).append("\'"));
        digestArgs(url).forEach((a) -> cmd.append(" \"").append(a).append("\""));

        //System.out.println(cmd);

        Process p = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", cmd.toString()});

        Thread t1 = null, t2 = null;
        if (stdoutMode || !stderrMode) {
            t1 = new Thread(() -> copyBytes(p.getInputStream(), System.out, noBuffer));
            t1.start();
        }
        if (stderrMode) {
            t2 = new Thread(() -> copyBytes(p.getErrorStream(), System.err, noBuffer));
            t2.start();
        }

        int exit = p.waitFor();

        if (t1 != null) {
            t1.join();
        }
        if (t2 != null) {
            t2.join();
        }

        System.exit(exit);
    }

    void checkStdoutMode(String arg) {
        switch (arg) {
            case "-o":
            case "-O": {
                stdoutMode = false;
                break;
            }
            case "-i":
            case "--include":
            case "-v":
            case "--verbose": {
                stderrMode = true;
                break;
            }
        }
    }

    int getArgsToSkip(String arg) {
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
        if (API_KEY == null) {
            throw new RuntimeException("API KEY not set");
        }

        if (API_SECRET == null) {
            throw new RuntimeException("API SECRET not set");
        }
    }

    private String interpolate(String arg) {
        for (Map.Entry<String, String> ent: replacements.entrySet()) {
            arg = arg.replaceAll("\\Q${" + ent.getKey() + "}\\E", ent.getValue());
        }
        return arg;
    }

    private void addData(String arg, String keyval, boolean encode) {
        String name = null;
        String value = null;
        if (keyval == null) {
            name = arg;
        } else {
            int idx = keyval.indexOf("=");
            name = idx == -1 ? keyval : keyval.substring(0, idx);
            value = idx == -1 ? null : keyval.substring(idx + 1);
        }
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


    List<String> digestArgs(String url) {

        List<String> args = new LinkedList<>();
        URI uri;
        try {
            uri = new URI(url);

            String path = uri.getPath();
            String query = uri.getQuery();

            String time = String.valueOf(System.currentTimeMillis());
            String nonce = UUID.randomUUID().toString();

            String input = API_KEY +
                           time +
                           nonce +
                           method +
                           (path == null ? "" : path) +
                           (query == null || "".equals(query) ? "" : "?" + query) +
                           body;

            String digest = hmacSha256(API_SECRET, input);

            // Uncomment the following to see real working values for authentication
/*
            System.out.println("Message Digest Inputs: [\n  apiKey: [" + API_KEY + "]\n  time: [" + time + "]\n  nonce: [" + nonce +
                               "]\n  method: [" + method + "]\n  path: [" + path + "]\n  query: [" +
                               (query == null || "".equals(query) ? "" : "?" + query) +
                               "]\n  body: [" + body + "]\n]");
            System.out.println("Message Digest Raw Input: [" + input + "]");
            System.out.println("Secret: [" + API_SECRET + "]");
            System.out.println("Message Digest (SHA-256): [" + digest + "]");
            System.out.println("X-Auth header: [" + API_KEY + ":" + digest + "]");
*/
            args.add("-H");
            args.add("X-Time: " + time);
            args.add("-H");
            args.add("X-Nonce: " + nonce);
            args.add("-H");
            args.add("X-Auth: " + API_KEY + ":" + digest);

        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate crypto signature: ", e);
        }

        return args;
    }

}
