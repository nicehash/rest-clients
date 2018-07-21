package com.nicehash.utils.cli;

import java.util.UUID;

public class CliUtils {

    public static void printError(boolean verbose, Throwable e) {
        if (verbose) {
            e.printStackTrace();
        } else {
            System.err.println("Error: " + e.getMessage());
            Throwable cause = e.getCause();
            while (cause != null) {
                System.out.println("  " + cause.getMessage());
                cause = cause.getCause();
            }
        }
    }

    public static String ensureArgument(String name, String[] args, int i) {
        if (i >= args.length) {
            throw new RuntimeException("Argument missing: " + name);
        }
        return args[i];
    }

    public static void ensureOption(String name, Object value) {
        if (value == null) {
            throw new RuntimeException("Option not set: " + name);
        }
    }

    public static void missOption(String name, Object value) {
        if (value != null) {
            throw new RuntimeException("Option not applicable in this case: " + name);
        }
    }

    public static String optValue(String option, String[] args, int i) {
        if (i >= args.length) {
            throw new IllegalArgumentException("Option " + option + " requires an argument");
        }
        return args[i];
    }

    public static String[] shift(String[] args) {
        if (args.length == 0) {
            return args;
        }
        String[] ret = new String[args.length - 1];
        System.arraycopy(args, 1, ret, 0, args.length - 1);
        return ret;
    }

    public static String getNicehashUrl(String defaultValue) {
        String value = System.getProperty("nicehash.url");
        if (value == null || value.length() == 0) {
            value = System.getenv("NICEHASH_URL");
        }

        value = value != null && value.length() > 0 ? value : defaultValue;
        value = value.endsWith("/") ? value : value + "/";
        return value;
    }

    public static String getNicehashWsUrl(String defaultValue) {
        String value = System.getProperty("nicehash.ws.url");
        if (value == null || value.length() == 0) {
            value = System.getenv("NICEHASH_WS_URL");
        }

        value = value != null && value.length() > 0 ? value : defaultValue;
        value = value.endsWith("/") ? value : value;
        return value;
    }

    public static String getApiKey(String defaultValue) {
        String value = System.getProperty("nicehash.api.key");
        if (value == null || value.length() == 0) {
            value = System.getenv("NICEHASH_API_KEY");
        }

        return value != null && value.length() > 0 ? value : defaultValue;
    }

    public static String getApiSecret(String defaultValue) {
        String value = System.getProperty("nicehash.api.secret");
        if (value == null || value.length() == 0) {
            value = System.getenv("NICEHASH_API_SECRET");
        }

        return value != null && value.length() > 0 ? value : defaultValue;
    }

    public static String getOrganisationId(String defaultValue) {
        String value = System.getProperty("nicehash.organisation.id");
        if (value == null || value.length() == 0) {
            value = System.getenv("NICEHASH_ORGANISATION_ID");
        }

        return value != null && value.length() > 0 ? value : defaultValue;
    }

    public static String getUserId(String defaultValue) {
        String value = System.getProperty("nicehash.user.id");
        if (value == null || value.length() == 0) {
            value = System.getenv("NICEHASH_USER_ID");
        }

        return value != null && value.length() > 0 ? value : defaultValue;
    }

    public static UUID asUUID(String name, String val) {
        try {
            return UUID.fromString(val);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid value for " + name + " - it should be a UUID (" + val + ")");
        }
    }
}
