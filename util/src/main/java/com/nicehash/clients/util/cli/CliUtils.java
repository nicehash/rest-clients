package com.nicehash.clients.util.cli;

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
        String value = getConfiguration("nicehash.url", defaultValue);
        value = value.endsWith("/") ? value : value + "/";
        return value;
    }

    public static String getNicehashWsUrl(String defaultValue) {
        String value = getConfiguration("nicehash.ws.url", defaultValue);
        value = value.endsWith("/") ? value : value;
        return value;
    }

    public static String getApiKey(String defaultValue) {
        return getConfiguration("nicehash.api.key", defaultValue);
    }

    public static String getApiSecret(String defaultValue) {
        return getConfiguration("nicehash.api.secret", defaultValue);
    }

    public static String getOrganisationId(String defaultValue) {
        return getConfiguration("nicehash.organisation.id", defaultValue);
    }

    public static String getUserId(String defaultValue) {
        return getConfiguration("nicehash.user.id", defaultValue);
    }

    public static String getConfiguration(String propertyName, String defaultValue) {
        String value = System.getProperty(propertyName);
        if (value == null || value.length() == 0) {
            value = System.getenv(toEnvPropertyName(propertyName));
        }
        return value != null && value.trim().length() > 0 ? value.trim() : defaultValue;
    }

    public static String toEnvPropertyName(String propertyName) {
        StringBuilder sb = new StringBuilder();
        for (char c: propertyName.toCharArray()) {
            if (Character.isUpperCase(c) || (c == '_')) {
                sb.append(c);
            } else if (Character.isLowerCase(c)) {
                sb.append(Character.toUpperCase(c));
            } else if (c == '-' || c == '.') {
                sb.append('_');
            } else {
                throw new IllegalArgumentException("Invalid character in property name ('" + propertyName + "'): '" + c + "'");
            }
        }
        return sb.toString();
    }

    public static UUID asUUID(String name, String val) {
        try {
            return UUID.fromString(val);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid value for " + name + " - it should be a UUID (" + val + ")");
        }
    }
}
