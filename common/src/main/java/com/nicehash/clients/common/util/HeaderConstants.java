package com.nicehash.clients.common.util;

/**
 * Header constants.
 */
public class HeaderConstants {
    /**
     * Content type
     */
    public static final String CONTENT_TYPE_APPLICATION_JSON = "Content-Type:application/json;charset=UTF-8";

    /**
     * HTTP Header to be used for API-KEY/SECRET authentication.
     */
    public static final String AUTH_HEADER = "X-Auth";
    public static final String TIME_HEADER = "X-Time";
    public static final String NONCE_HEADER = "X-Nonce";
    public static final String ORGANIZATION_HEADER = "X-Organization-Id";

    /**
     * Decorator to indicate that an endpoint requires an API key.
     */
    public static final String ENDPOINT_SECURITY_TYPE_APIKEY = "APIKEY";
    public static final String ENDPOINT_SECURITY_TYPE_APIKEY_HEADER = ENDPOINT_SECURITY_TYPE_APIKEY + ": #";

    /**
     * Decorator to indicate that an endpoint requires a signature.
     */
    public static final String ENDPOINT_SECURITY_TYPE_SIGNED = "SIGNED";
    public static final String ENDPOINT_SECURITY_TYPE_SIGNED_HEADER = ENDPOINT_SECURITY_TYPE_SIGNED + ": #";
}
